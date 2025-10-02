package com.kone.kitms.web.rest;

import com.kone.kitms.security.FileSecurityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * KITMS 파일 업로드 관리 REST API 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 파일 업로드 관리를 위한 모든 기능을 제공합니다:
 * - 이미지 파일 업로드 (CKEditor용)
 * - 일반 파일 업로드
 * - 파일 유효성 검사 (크기, 타입)
 * - 고유한 파일명 생성
 * - 날짜별 디렉토리 구조 관리
 * - 에러 처리 및 응답 관리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    @Value("${app.upload.path:uploads}")
    private String uploadPath;

    @Autowired
    private FileSecurityValidator fileSecurityValidator;

    /**
     * 이미지 업로드 (CKEditor용)
     */
    @PostMapping("/image")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("upload") MultipartFile file) {
        log.info("이미지 업로드 요청: {}", file.getOriginalFilename());
        
        try {
            // 파일 유효성 검사
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("파일이 비어있습니다."));
            }

            // 파일 크기 검사 (5MB 제한)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(createErrorResponse("파일 크기는 5MB를 초과할 수 없습니다."));
            }

            // 보안 검증 - 파일 시그니처 및 확장자 검증
            FileSecurityValidator.ValidationResult validationResult = 
                fileSecurityValidator.validateImageFile(file.getOriginalFilename(), file.getInputStream());
            
            if (!validationResult.isValid()) {
                log.warn("이미지 파일 보안 검증 실패: {} - {}", file.getOriginalFilename(), validationResult.getErrorMessage());
                return ResponseEntity.badRequest().body(createErrorResponse(validationResult.getErrorMessage()));
            }

            // 업로드 디렉토리 생성
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String fullUploadPath = uploadPath + "/images/" + datePath;
            File uploadDir = new File(fullUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 고유한 파일명 생성
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // 파일 저장
            Path filePath = Paths.get(fullUploadPath, uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // 웹 접근 가능한 URL 생성
            String fileUrl = "/uploads/images/" + datePath + "/" + uniqueFilename;

            // CKEditor 5 Classic 응답 형식
            Map<String, Object> response = new HashMap<>();
            response.put("url", fileUrl);

            log.info("이미지 업로드 성공: {}", fileUrl);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("이미지 업로드 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("파일 업로드 중 오류가 발생했습니다."));
        }
    }

    /**
     * 일반 파일 업로드
     */
    @PostMapping("/file")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("파일 업로드 요청: {}", file.getOriginalFilename());
        
        try {
            // 파일 유효성 검사
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("파일이 비어있습니다."));
            }

            // 파일 크기 검사 (10MB 제한)
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(createErrorResponse("파일 크기는 10MB를 초과할 수 없습니다."));
            }

            // 보안 검증 - 파일 시그니처 및 확장자 검증
            FileSecurityValidator.ValidationResult validationResult = 
                fileSecurityValidator.validateDocumentFile(file.getOriginalFilename(), file.getInputStream());
            
            if (!validationResult.isValid()) {
                log.warn("파일 보안 검증 실패: {} - {}", file.getOriginalFilename(), validationResult.getErrorMessage());
                return ResponseEntity.badRequest().body(createErrorResponse(validationResult.getErrorMessage()));
            }

            // 업로드 디렉토리 생성
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String fullUploadPath = uploadPath + "/files/" + datePath;
            File uploadDir = new File(fullUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 고유한 파일명 생성
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // 파일 저장
            Path filePath = Paths.get(fullUploadPath, uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // 웹 접근 가능한 URL 생성
            String fileUrl = "/uploads/files/" + datePath + "/" + uniqueFilename;

            // 응답 데이터
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("url", fileUrl);
            response.put("filename", uniqueFilename);
            response.put("originalFilename", originalFilename);
            response.put("size", file.getSize());

            log.info("파일 업로드 성공: {}", fileUrl);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("파일 업로드 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("파일 업로드 중 오류가 발생했습니다."));
        }
    }

    /**
     * 에러 응답 생성
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", Map.of("message", message));
        return errorResponse;
    }
}
