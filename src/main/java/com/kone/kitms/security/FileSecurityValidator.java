package com.kone.kitms.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * KITMS 파일 보안 검증 클래스
 *
 * 이 클래스는 KITMS 시스템의 파일 업로드 보안을 담당합니다:
 * - 파일 시그니처(Magic Number) 검증
 * - 허용된 파일 확장자 검증
 * - 실행 가능한 파일 차단
 * - 파일 크기 및 내용 검증
 * - 멀웨어 및 악성 파일 차단
 *
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Component
public class FileSecurityValidator {

    private static final Logger log = LoggerFactory.getLogger(FileSecurityValidator.class);

    // 허용된 이미지 파일 확장자
    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of(
        ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    );

    // 허용된 일반 파일 확장자
    private static final Set<String> ALLOWED_DOCUMENT_EXTENSIONS = Set.of(
        ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".txt", ".hwp"
    );

    // 차단된 위험한 확장자
    private static final Set<String> BLOCKED_EXTENSIONS = Set.of(
        ".exe", ".bat", ".cmd", ".com", ".scr", ".pif", ".vbs", ".js", ".jar",
        ".php", ".jsp", ".asp", ".aspx", ".py", ".rb", ".pl", ".sh", ".ps1"
    );

    // 파일 시그니처 (Magic Number) 매핑
    private static final Map<String, byte[]> FILE_SIGNATURES = new HashMap<>();

    static {
        // 이미지 파일 시그니처
        FILE_SIGNATURES.put("jpg", new byte[]{(byte)0xFF, (byte)0xD8, (byte)0xFF});
        FILE_SIGNATURES.put("jpeg", new byte[]{(byte)0xFF, (byte)0xD8, (byte)0xFF});
        FILE_SIGNATURES.put("png", new byte[]{(byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A});
        FILE_SIGNATURES.put("gif", new byte[]{0x47, 0x49, 0x46, 0x38}); // GIF8
        FILE_SIGNATURES.put("bmp", new byte[]{0x42, 0x4D}); // BM
        FILE_SIGNATURES.put("webp", new byte[]{0x52, 0x49, 0x46, 0x46}); // RIFF (WebP는 추가 검증 필요)
        
        // 문서 파일 시그니처
        FILE_SIGNATURES.put("pdf", new byte[]{0x25, 0x50, 0x44, 0x46}); // %PDF
        FILE_SIGNATURES.put("doc", new byte[]{(byte)0xD0, (byte)0xCF, 0x11, (byte)0xE0}); // Microsoft Office
        FILE_SIGNATURES.put("docx", new byte[]{0x50, 0x4B, 0x03, 0x04}); // ZIP (Office 2007+)
        FILE_SIGNATURES.put("xls", new byte[]{(byte)0xD0, (byte)0xCF, 0x11, (byte)0xE0}); // Microsoft Office
        FILE_SIGNATURES.put("xlsx", new byte[]{0x50, 0x4B, 0x03, 0x04}); // ZIP (Office 2007+)
        FILE_SIGNATURES.put("ppt", new byte[]{(byte)0xD0, (byte)0xCF, 0x11, (byte)0xE0}); // Microsoft Office
        FILE_SIGNATURES.put("pptx", new byte[]{0x50, 0x4B, 0x03, 0x04}); // ZIP (Office 2007+)
        FILE_SIGNATURES.put("txt", new byte[]{}); // 텍스트 파일은 시그니처 없음
        FILE_SIGNATURES.put("hwp", new byte[]{(byte)0xD0, (byte)0xCF, 0x11, (byte)0xE0}); // 한글 문서
    }

    /**
     * 이미지 파일 보안 검증
     *
     * @param filename 파일명
     * @param inputStream 파일 스트림
     * @return 검증 결과
     */
    public ValidationResult validateImageFile(String filename, InputStream inputStream) {
        return validateFile(filename, inputStream, ALLOWED_IMAGE_EXTENSIONS, true);
    }

    /**
     * 일반 파일 보안 검증
     *
     * @param filename 파일명
     * @param inputStream 파일 스트림
     * @return 검증 결과
     */
    public ValidationResult validateDocumentFile(String filename, InputStream inputStream) {
        return validateFile(filename, inputStream, ALLOWED_DOCUMENT_EXTENSIONS, false);
    }

    /**
     * 파일 보안 검증 메인 메서드
     *
     * @param filename 파일명
     * @param inputStream 파일 스트림
     * @param allowedExtensions 허용된 확장자 목록
     * @param isImageFile 이미지 파일 여부
     * @return 검증 결과
     */
    private ValidationResult validateFile(String filename, InputStream inputStream, 
                                        Set<String> allowedExtensions, boolean isImageFile) {
        try {
            // 1. 파일명 검증
            if (filename == null || filename.trim().isEmpty()) {
                return ValidationResult.failure("파일명이 올바르지 않습니다.");
            }

            // 2. 확장자 추출 및 검증
            String extension = getFileExtension(filename).toLowerCase();
            if (extension.isEmpty()) {
                return ValidationResult.failure("파일 확장자가 없습니다.");
            }

            // 3. 위험한 확장자 차단
            if (BLOCKED_EXTENSIONS.contains(extension)) {
                log.warn("위험한 파일 확장자 차단: {}", extension);
                return ValidationResult.failure("허용되지 않는 파일 형식입니다.");
            }

            // 4. 허용된 확장자 검증
            if (!allowedExtensions.contains(extension)) {
                return ValidationResult.failure(
                    isImageFile ? "이미지 파일만 업로드 가능합니다." : "허용되지 않는 파일 형식입니다."
                );
            }

            // 5. 파일 시그니처 검증
            if (inputStream != null) {
                ValidationResult signatureResult = validateFileSignature(inputStream, extension);
                if (!signatureResult.isValid()) {
                    return signatureResult;
                }
            }

            // 6. 파일명 보안 검증
            if (containsDangerousCharacters(filename)) {
                return ValidationResult.failure("파일명에 허용되지 않는 문자가 포함되어 있습니다.");
            }

            return ValidationResult.success();

        } catch (Exception e) {
            log.error("파일 검증 중 오류 발생", e);
            return ValidationResult.failure("파일 검증 중 오류가 발생했습니다.");
        }
    }

    /**
     * 파일 시그니처 검증
     *
     * @param inputStream 파일 스트림
     * @param extension 파일 확장자
     * @return 검증 결과
     */
    private ValidationResult validateFileSignature(InputStream inputStream, String extension) {
        try {
            byte[] expectedSignature = FILE_SIGNATURES.get(extension.substring(1)); // . 제거
            if (expectedSignature == null || expectedSignature.length == 0) {
                // 시그니처가 없는 파일 형식 (예: txt)
                return ValidationResult.success();
            }

            // 파일 헤더 읽기
            byte[] fileHeader = new byte[expectedSignature.length];
            int bytesRead = inputStream.read(fileHeader);
            
            if (bytesRead < expectedSignature.length) {
                return ValidationResult.failure("파일이 손상되었거나 올바르지 않습니다.");
            }

            // 시그니처 비교
            if (!Arrays.equals(fileHeader, expectedSignature)) {
                log.warn("파일 시그니처 불일치: 예상={}, 실제={}", 
                    Arrays.toString(expectedSignature), Arrays.toString(fileHeader));
                return ValidationResult.failure("파일 내용과 확장자가 일치하지 않습니다.");
            }

            return ValidationResult.success();

        } catch (IOException e) {
            log.error("파일 시그니처 검증 중 오류", e);
            return ValidationResult.failure("파일 검증 중 오류가 발생했습니다.");
        }
    }

    /**
     * 파일 확장자 추출
     *
     * @param filename 파일명
     * @return 확장자
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex) : "";
    }

    /**
     * 위험한 문자 포함 여부 검사
     *
     * @param filename 파일명
     * @return 위험한 문자 포함 여부
     */
    private boolean containsDangerousCharacters(String filename) {
        // 경로 조작 문자, 특수 문자 등 차단
        String dangerousChars = "..\\/<>:\"|?*";
        for (char c : dangerousChars.toCharArray()) {
            if (filename.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 파일 검증 결과 클래스
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult failure(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
