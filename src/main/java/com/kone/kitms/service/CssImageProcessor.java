package com.kone.kitms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CSS 이미지 처리 서비스
 * CSS 파일의 background 이미지 URL을 보안 URL로 변환합니다.
 */
@Service
public class CssImageProcessor {

    @Autowired
    private ImagePathService imagePathService;

    // CSS background 이미지 URL 패턴
    private static final Pattern BACKGROUND_URL_PATTERN = Pattern.compile(
        "background(?:-image)?\\s*:\\s*url\\(['\"]?([^'\")]+)['\"]?\\)",
        Pattern.CASE_INSENSITIVE
    );

    // 이미지 확장자 패턴
    private static final Pattern IMAGE_EXTENSION_PATTERN = Pattern.compile(
        "\\.(png|jpg|jpeg|gif|svg|ico|webp)(\\?.*)?$",
        Pattern.CASE_INSENSITIVE
    );

    /**
     * CSS 파일의 이미지 URL을 보안 URL로 변환
     */
    public String processCssFile(String cssContent, Map<String, String> imageTokenMap) {
        if (cssContent == null || imageTokenMap == null) {
            return cssContent;
        }

        Matcher matcher = BACKGROUND_URL_PATTERN.matcher(cssContent);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String originalUrl = matcher.group(1);
            String secureUrl = convertToSecureUrl(originalUrl, imageTokenMap);
            
            // 변환된 URL로 교체
            matcher.appendReplacement(result, 
                matcher.group(0).replace(originalUrl, secureUrl));
        }
        
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * 이미지 URL을 보안 URL로 변환
     */
    private String convertToSecureUrl(String originalUrl, Map<String, String> imageTokenMap) {
        // 상대 경로인 경우만 처리
        if (!originalUrl.startsWith("http") && !originalUrl.startsWith("data:")) {
            // 파일명 추출
            String fileName = extractFileName(originalUrl);
            
            if (fileName != null && isImageFile(fileName)) {
                // 토큰 매핑에서 찾기
                for (Map.Entry<String, String> entry : imageTokenMap.entrySet()) {
                    if (entry.getKey().endsWith(fileName)) {
                        return "/api/secure-images/" + entry.getValue();
                    }
                }
            }
        }
        
        return originalUrl; // 변환할 수 없으면 원본 반환
    }

    /**
     * URL에서 파일명 추출
     */
    private String extractFileName(String url) {
        // 쿼리 파라미터 제거
        String cleanUrl = url.split("\\?")[0];
        
        // 경로에서 파일명 추출
        String[] parts = cleanUrl.split("/");
        return parts.length > 0 ? parts[parts.length - 1] : null;
    }

    /**
     * 이미지 파일 여부 확인
     */
    private boolean isImageFile(String fileName) {
        return IMAGE_EXTENSION_PATTERN.matcher(fileName).find();
    }

    /**
     * CSS 파일을 읽어서 처리된 내용 반환
     */
    public String processCssFileFromPath(String cssFilePath, Map<String, String> imageTokenMap) {
        try {
            Resource resource = new ClassPathResource(cssFilePath);
            if (resource.exists()) {
                String cssContent = Files.readString(resource.getFile().toPath());
                return processCssFile(cssContent, imageTokenMap);
            }
        } catch (IOException e) {
            System.err.println("CSS 파일 처리 중 오류: " + e.getMessage());
        }
        return null;
    }

    /**
     * 모든 CSS 파일의 이미지 URL을 일괄 처리
     */
    public Map<String, String> processAllCssFiles(Map<String, String> imageTokenMap) {
        Map<String, String> processedCssFiles = new HashMap<>();
        
        String[] cssFiles = {
            "static/style/main.css",
            "static/style/common.css",
            "static/style/reset.css",
            "static/style/mobile.css"
        };

        for (String cssFile : cssFiles) {
            String processedContent = processCssFileFromPath(cssFile, imageTokenMap);
            if (processedContent != null) {
                processedCssFiles.put(cssFile, processedContent);
            }
        }

        return processedCssFiles;
    }
}
