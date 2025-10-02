package com.kone.kitms.web.rest;

import com.kone.kitms.aop.logging.ExTokenCheck;
import com.kone.kitms.service.ImagePathService;
import com.kone.kitms.service.CssImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * KITMS 보안 이미지 관리 컨트롤러
 * 
 * 이 클래스는 이미지 파일의 보안 접근을 관리하는 기능을 제공합니다:
 * - 토큰 기반 이미지 접근 제어
 * - 세션 토큰 관리
 * - 이미지 토큰 자동 생성 및 관리
 * - CSS 파일의 이미지 URL 보안 처리
 * - 토큰 만료 시간 관리
 * - 직접 파일 접근 방지
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/secure-images")
public class SecureImageController {

    @Autowired
    private ImagePathService imagePathService;
    
    @Autowired
    private CssImageProcessor cssImageProcessor;

    // 이미지 파일 경로와 임시 토큰 매핑 (동적 생성)
    private static final Map<String, String> imageTokens = new HashMap<>();
    
    // 토큰 생성 시간 저장 (만료 시간 관리용)
    private static final Map<String, Long> tokenTimestamps = new HashMap<>();
    
    // 세션별 토큰 매핑 (세션ID -> 토큰)
    private static final Map<String, String> sessionTokens = new HashMap<>();
    
    // 세션 토큰 생성 시간 저장
    private static final Map<String, Long> sessionTokenTimestamps = new HashMap<>();
    
    // 세션 토큰 만료 시간 (2시간)
    private static final long SESSION_TOKEN_EXPIRY_TIME = 2 * 60 * 60 * 1000;
    
    // 개별 이미지 토큰 만료 시간 (2시간)
    private static final long IMAGE_TOKEN_EXPIRY_TIME = 2 * 60 * 60 * 1000;

    // 이미지 확장자 목록
    private static final String[] IMAGE_EXTENSIONS = {".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico", ".webp"};
    
    static {
        // 서버 시작 시 자동으로 모든 이미지 파일을 스캔하여 토큰 생성
        initializeImageTokens();
    }
    
    /**
     * 이미지 토큰 초기화 (자동 스캔)
     */
    private static void initializeImageTokens() {
        try {
            // images 디렉토리에서 모든 이미지 파일 스캔
            scanImageDirectory("static/images/");
        } catch (Exception e) {
            System.err.println("이미지 토큰 초기화 중 오류 발생: " + e.getMessage());
        }
    }
    
    /**
     * 디렉토리에서 이미지 파일 스캔
     */
    private static void scanImageDirectory(String basePath) {
        try {
            // ClassPathResource를 사용하여 디렉토리 스캔
            ClassPathResource resource = new ClassPathResource(basePath);
            if (resource.exists()) {
                // 실제 파일 시스템 경로로 변환
                Path imagesPath = Paths.get("src/main/resources/" + basePath);
                if (Files.exists(imagesPath)) {
                    Files.walk(imagesPath)
                            .filter(Files::isRegularFile)
                            .filter(path -> isImageFile(path.getFileName().toString()))
                            .forEach(path -> {
                                String relativePath = imagesPath.relativize(path).toString().replace("\\", "/");
                                String token = generateToken();
                                imageTokens.put(relativePath, token);
                                tokenTimestamps.put(token, System.currentTimeMillis());
                            });
                }
            }
        } catch (Exception e) {
            System.err.println("디렉토리 스캔 중 오류: " + e.getMessage());
        }
    }
    
    /**
     * 이미지 파일 여부 확인
     */
    private static boolean isImageFile(String fileName) {
        String lowerFileName = fileName.toLowerCase();
        for (String ext : IMAGE_EXTENSIONS) {
            if (lowerFileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 토큰으로 이미지 제공
     */
    @GetMapping("/{token}")
    public ResponseEntity<byte[]> getSecureImage(@PathVariable String token, 
                                                HttpServletRequest request) {
        try {
            // 보안 검증
            if (!isValidRequest(request)) {
                return ResponseEntity.status(403).build();
            }
            
            // 토큰으로 파일명 찾기
            String fileName = findFileNameByToken(token);
            if (fileName == null) {
                return ResponseEntity.notFound().build();
            }
            
            // 토큰 만료 시간 검증
            if (isTokenExpired(token)) {
                // 토큰이 만료된 경우 자동으로 새 토큰 생성
                String newToken = regenerateTokenForFile(fileName);
                if (newToken != null) {
                    // 새 토큰으로 리다이렉트
                    return ResponseEntity.status(302)
                            .header("Location", "/api/secure-images/" + newToken)
                            .build();
                } else {
                    return ResponseEntity.status(410).build(); // Gone
                }
            }
            
            // 이미지 파일 로드 (폴더 구조 지원)
            Resource resource = findImageResource(fileName);
            if (resource == null || !resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            // 파일을 바이트 배열로 읽기
            byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
            
            // Content-Type 설정
            String contentType = getContentType(fileName);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .header("X-Content-Type-Options", "nosniff")
                    .header("X-Frame-Options", "DENY")
                    .body(imageBytes);
                    
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 세션 토큰 발급 (페이지 로드 시 호출)
     */
    @PostMapping("/session-token")
    public ResponseEntity<Map<String, Object>> generateSessionToken(HttpServletRequest request) {
        try {
            String sessionId = request.getSession().getId();
            
            // 항상 새로운 세션 토큰 생성 (새로고침할 때마다)
            String sessionToken = generateToken();
            sessionTokens.put(sessionId, sessionToken);
            sessionTokenTimestamps.put(sessionId, System.currentTimeMillis());
            
            Map<String, Object> response = new HashMap<>();
            response.put("sessionToken", sessionToken);
            response.put("expiresIn", SESSION_TOKEN_EXPIRY_TIME);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("세션 토큰 생성 오류: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "세션 토큰 생성 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 이미지 토큰 매핑 정보 제공
     */
    @GetMapping("/mapping")
    public ResponseEntity<Map<String, String>> getImageMappings() {
        Map<String, String> mappings = new HashMap<>();
        
        for (Map.Entry<String, String> entry : imageTokens.entrySet()) {
            mappings.put(entry.getKey(), "/api/secure-images/" + entry.getValue());
        }
        
        return ResponseEntity.ok(mappings);
    }
    
    /**
     * 특정 파일명으로 토큰 생성 (동적 생성)
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("SecureImageController가 정상적으로 작동합니다!");
    }
    
    @GetMapping("/generate/**")
    public ResponseEntity<Map<String, String>> generateTokenForFile(HttpServletRequest request) {
        // 요청 URI에서 파일명 추출
        String requestURI = request.getRequestURI();
        String fileName = requestURI.substring("/api/secure-images/generate/".length());
        
        // URL 디코딩 처리
        String decodedFileName = fileName;
        try {
            decodedFileName = java.net.URLDecoder.decode(fileName, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            System.out.println("❌ URL 디코딩 실패: " + fileName);
        }
        final String finalDecodedFileName = decodedFileName;
        
        try {
            // 파일이 존재하는지 확인
            String imagePath = imagePathService.findImagePath(finalDecodedFileName);
            
            Resource resource;
            
            // 외부 파일인지 확인 (images/로 시작하는 경우)
            if (imagePath.startsWith("images/")) {
                resource = new FileSystemResource(imagePath);
            } else {
                resource = new ClassPathResource(imagePath);
            }
            
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            
            // 토큰 생성 (세션별로 고유한 토큰 생성)
            String sessionId = request.getSession().getId();
            String tokenKey = sessionId + ":" + finalDecodedFileName;
            String token = imageTokens.computeIfAbsent(tokenKey, k -> {
                String newToken = generateToken();
                tokenTimestamps.put(newToken, System.currentTimeMillis());
                // 세션 토큰도 저장
                sessionTokens.put(sessionId, newToken);
                sessionTokenTimestamps.put(sessionId, System.currentTimeMillis());
                return newToken;
            });
            
            Map<String, String> response = new HashMap<>();
            response.put("fileName", finalDecodedFileName);
            response.put("token", token);
            response.put("secureUrl", "/api/secure-images/" + token);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 모든 이미지 토큰 새로고침
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshImageTokens() {
        try {
            // 기존 토큰 초기화
            imageTokens.clear();
            
            // 새로 스캔하여 토큰 생성
            initializeImageTokens();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "이미지 토큰이 새로고침되었습니다.");
            response.put("totalImages", imageTokens.size());
            response.put("tokens", imageTokens);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "토큰 새로고침 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * CSS 파일을 보안 URL로 처리하여 제공
     */
    @GetMapping("/css/{cssFileName}")
    public ResponseEntity<String> getSecureCssFile(@PathVariable String cssFileName) {
        try {
            // CSS 파일 경로
            String cssFilePath = "static/style/" + cssFileName;
            Resource resource = new ClassPathResource(cssFilePath);
            
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            // CSS 파일 내용 읽기
            String cssContent = Files.readString(resource.getFile().toPath());
            
            // 이미지 URL을 보안 URL로 변환
            String processedCss = cssImageProcessor.processCssFile(cssContent, imageTokens);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("text/css"))
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .body(processedCss);
                    
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 모든 CSS 파일의 보안 URL 처리 결과 제공
     */
    @GetMapping("/css/processed")
    public ResponseEntity<Map<String, String>> getAllProcessedCssFiles() {
        try {
            Map<String, String> processedCssFiles = cssImageProcessor.processAllCssFiles(imageTokens);
            return ResponseEntity.ok(processedCssFiles);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "CSS 파일 처리 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 요청 유효성 검증
     */
    private boolean isValidRequest(HttpServletRequest request) {
        // 1. 세션 토큰 검증 (보안 강화)
        if (!isValidSessionToken(request)) {
            return false;
        }
        
        // 2. User-Agent 검증 - 브라우저에서 온 요청인지 확인
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.trim().isEmpty()) {
            return false;
        }
        
        // 3. Referer 헤더 검증 - 허용된 도메인에서 온 요청인지 확인
        String referer = request.getHeader("Referer");
        if (referer == null || !isValidReferer(referer, request)) {
            return false;
        }
        
        // 4. Accept 헤더 검증 - 이미지를 요청하는지 확인
        String accept = request.getHeader("Accept");
        if (accept == null || (!accept.contains("image") && !accept.contains("*/*"))) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 토큰 만료 여부 확인
     */
    private boolean isTokenExpired(String token) {
        Long timestamp = tokenTimestamps.get(token);
        if (timestamp == null) {
            return true; // 타임스탬프가 없으면 만료된 것으로 간주
        }
        
        long currentTime = System.currentTimeMillis();
        return (currentTime - timestamp) > IMAGE_TOKEN_EXPIRY_TIME;
    }
    
    /**
     * 세션 토큰 만료 여부 확인
     */
    private boolean isSessionTokenExpired(String sessionId) {
        Long timestamp = sessionTokenTimestamps.get(sessionId);
        if (timestamp == null) {
            return true; // 타임스탬프가 없으면 만료된 것으로 간주
        }
        
        long currentTime = System.currentTimeMillis();
        return (currentTime - timestamp) > SESSION_TOKEN_EXPIRY_TIME;
    }
    
    /**
     * 세션 토큰 유효성 검증
     */
    private boolean isValidSessionToken(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return sessionTokens.containsKey(sessionId) && !isSessionTokenExpired(sessionId);
    }
    
    /**
     * Referer 헤더 유효성 검증
     */
    private boolean isValidReferer(String referer, HttpServletRequest request) {
        try {
            // 현재 요청의 호스트 정보 가져오기
            String host = request.getHeader("Host");
            if (host == null) {
                host = request.getServerName() + ":" + request.getServerPort();
            }
            
            // Referer가 현재 호스트와 일치하는지 확인
            return referer.contains(host);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 토큰으로 파일명 찾기 (세션 기반)
     */
    private String findFileNameByToken(String token) {
        for (Map.Entry<String, String> entry : imageTokens.entrySet()) {
            if (entry.getValue().equals(token)) {
                String tokenKey = entry.getKey();
                // 세션ID:파일명 형식에서 파일명만 추출
                if (tokenKey.contains(":")) {
                    return tokenKey.substring(tokenKey.indexOf(":") + 1);
                }
                return tokenKey;
            }
        }
        return null;
    }
    
    /**
     * 파일명으로 토큰 재생성
     */
    private String regenerateTokenForFile(String fileName) {
        try {
            // 파일이 존재하는지 확인
            String imagePath = imagePathService.findImagePath(fileName);
            Resource resource = new ClassPathResource(imagePath);
            
            if (!resource.exists()) {
                return null;
            }
            
            // 새 토큰 생성
            String newToken = generateToken();
            
            // 기존 토큰 제거
            imageTokens.entrySet().removeIf(entry -> entry.getKey().equals(fileName));
            tokenTimestamps.entrySet().removeIf(entry -> entry.getKey().equals(imageTokens.get(fileName)));
            
            // 새 토큰 등록
            imageTokens.put(fileName, newToken);
            tokenTimestamps.put(newToken, System.currentTimeMillis());
            
            return newToken;
            
        } catch (Exception e) {
            System.err.println("토큰 재생성 중 오류: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 이미지 리소스 찾기 (폴더 구조 지원)
     */
    private Resource findImageResource(String fileName) {
        // ImagePathService를 사용하여 경로 찾기
        String imagePath = imagePathService.findImagePath(fileName);
        Resource resource;
        
        // 외부 파일인지 확인 (images/로 시작하는 경우)
        if (imagePath.startsWith("images/")) {
            resource = new FileSystemResource(imagePath);
        } else {
            resource = new ClassPathResource(imagePath);
        }
        
        if (resource.exists()) {
            return resource;
        }
        
        // 서비스에서 찾지 못한 경우 기존 방식으로 시도
        return findImageByFileName(fileName);
    }
    
    /**
     * 파일명으로 이미지 찾기 (하위 폴더 포함)
     */
    private Resource findImageByFileName(String fileName) {
        try {
            // 실제 파일 시스템에서 검색
            Path imagesDir = Paths.get("src/main/resources/static/images");
            if (Files.exists(imagesDir)) {
                return findFileInDirectory(imagesDir, fileName);
            }
        } catch (Exception e) {
            // 파일 시스템 검색 실패 시 null 반환
        }
        return null;
    }
    
    /**
     * 디렉토리에서 파일 재귀적으로 찾기
     */
    private Resource findFileInDirectory(Path directory, String fileName) {
        try {
            return Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .findFirst()
                    .map(path -> new ClassPathResource("static/images/" + 
                            directory.relativize(path).toString().replace("\\", "/")))
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 토큰 생성
     */
    private static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 파일 확장자에 따른 Content-Type 반환
     */
    private String getContentType(String fileName) {
        String extension = fileName.toLowerCase();
        if (extension.endsWith(".png")) return "image/png";
        if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) return "image/jpeg";
        if (extension.endsWith(".gif")) return "image/gif";
        if (extension.endsWith(".svg")) return "image/svg+xml";
        if (extension.endsWith(".ico")) return "image/x-icon";
        if (extension.endsWith(".webp")) return "image/webp";
        return "application/octet-stream";
    }
}
