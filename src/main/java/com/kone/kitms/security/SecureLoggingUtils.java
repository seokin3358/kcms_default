package com.kone.kitms.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 보안 로깅 유틸리티 클래스
 * 
 * 이 클래스는 로그에 민감한 정보가 기록되지 않도록 하는 기능을 제공합니다:
 * - 비밀번호, 토큰, 키 등 민감한 정보 마스킹
 * - SQL 쿼리에서 민감한 파라미터 값 마스킹
 * - HTTP 요청/응답에서 민감한 헤더 값 마스킹
 * - 개인정보(이메일, 전화번호 등) 마스킹
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Component
public class SecureLoggingUtils {

    private static final Logger log = LoggerFactory.getLogger(SecureLoggingUtils.class);

    // 민감한 정보 패턴들
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("(?i)(password|pwd|pass)\\s*[:=]\\s*[^\\s,}]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern TOKEN_PATTERN = Pattern.compile("(?i)(token|auth|authorization|bearer)\\s*[:=]\\s*[^\\s,}]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern KEY_PATTERN = Pattern.compile("(?i)(key|secret|private|public)\\s*[:=]\\s*[^\\s,}]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\b(\\d{2,3})-?(\\d{3,4})-?(\\d{4})\\b");
    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile("\\b\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}\\b");

    /**
     * 로그 메시지에서 민감한 정보를 마스킹합니다.
     * 
     * @param message 원본 로그 메시지
     * @return 민감한 정보가 마스킹된 로그 메시지
     */
    public static String maskSensitiveData(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }

        String maskedMessage = message;

        // 비밀번호 마스킹
        maskedMessage = PASSWORD_PATTERN.matcher(maskedMessage).replaceAll("$1=***");
        
        // 토큰 마스킹
        maskedMessage = TOKEN_PATTERN.matcher(maskedMessage).replaceAll("$1=***");
        
        // 키/시크릿 마스킹
        maskedMessage = KEY_PATTERN.matcher(maskedMessage).replaceAll("$1=***");
        
        // 이메일 마스킹 (부분 마스킹)
        maskedMessage = EMAIL_PATTERN.matcher(maskedMessage).replaceAll(matchResult -> {
            String email = matchResult.group();
            String[] parts = email.split("@");
            if (parts[0].length() > 2) {
                return parts[0].substring(0, 2) + "***@" + parts[1];
            }
            return "***@" + parts[1];
        });
        
        // 전화번호 마스킹
        maskedMessage = PHONE_PATTERN.matcher(maskedMessage).replaceAll("$1-****-$3");
        
        // 신용카드 번호 마스킹
        maskedMessage = CREDIT_CARD_PATTERN.matcher(maskedMessage).replaceAll("****-****-****-****");

        return maskedMessage;
    }

    /**
     * SQL 쿼리에서 민감한 파라미터를 마스킹합니다.
     * 
     * @param sql 원본 SQL 쿼리
     * @return 민감한 파라미터가 마스킹된 SQL 쿼리
     */
    public static String maskSqlParameters(String sql) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }

        // SQL 파라미터 바인딩 패턴 (?, :paramName 등)
        String maskedSql = sql.replaceAll("\\?\\s*=\\s*'[^']*'", "? = '***'");
        maskedSql = maskedSql.replaceAll("\\?\\s*=\\s*[^\\s,)]+", "? = ***");
        
        // Named parameter 바인딩
        maskedSql = maskedSql.replaceAll(":[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*'[^']*'", ":$1 = '***'");
        maskedSql = maskedSql.replaceAll(":[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*[^\\s,)]+", ":$1 = ***");

        return maskedSql;
    }

    /**
     * HTTP 헤더에서 민감한 정보를 마스킹합니다.
     * 
     * @param headerName 헤더 이름
     * @param headerValue 헤더 값
     * @return 마스킹된 헤더 정보
     */
    public static String maskHttpHeader(String headerName, String headerValue) {
        if (headerName == null || headerValue == null) {
            return headerValue;
        }

        String lowerHeaderName = headerName.toLowerCase();
        
        // 민감한 헤더들
        if (lowerHeaderName.contains("authorization") || 
            lowerHeaderName.contains("cookie") || 
            lowerHeaderName.contains("x-api-key") ||
            lowerHeaderName.contains("x-auth-token") ||
            lowerHeaderName.contains("kitms-token")) {
            return "***";
        }

        return headerValue;
    }

    /**
     * 안전한 로그 기록을 위한 래퍼 메서드
     * 
     * @param logger 로거 인스턴스
     * @param level 로그 레벨 (DEBUG, INFO, WARN, ERROR)
     * @param message 로그 메시지
     * @param throwable 예외 (선택사항)
     */
    public static void logSafely(Logger logger, String level, String message, Throwable throwable) {
        String maskedMessage = maskSensitiveData(message);
        
        switch (level.toUpperCase()) {
            case "DEBUG":
                if (throwable != null) {
                    logger.debug(maskedMessage, throwable);
                } else {
                    logger.debug(maskedMessage);
                }
                break;
            case "INFO":
                if (throwable != null) {
                    logger.info(maskedMessage, throwable);
                } else {
                    logger.info(maskedMessage);
                }
                break;
            case "WARN":
                if (throwable != null) {
                    logger.warn(maskedMessage, throwable);
                } else {
                    logger.warn(maskedMessage);
                }
                break;
            case "ERROR":
                if (throwable != null) {
                    logger.error(maskedMessage, throwable);
                } else {
                    logger.error(maskedMessage);
                }
                break;
            default:
                logger.info(maskedMessage);
        }
    }

    /**
     * SQL 쿼리 로그를 안전하게 기록합니다.
     * 
     * @param logger 로거 인스턴스
     * @param sql SQL 쿼리
     * @param parameters 파라미터 (선택사항)
     */
    public static void logSqlSafely(Logger logger, String sql, Object... parameters) {
        String maskedSql = maskSqlParameters(sql);
        String message = "SQL: " + maskedSql;
        
        if (parameters != null && parameters.length > 0) {
            message += " | Parameters: [";
            for (int i = 0; i < parameters.length; i++) {
                if (i > 0) message += ", ";
                message += "***"; // 모든 파라미터 마스킹
            }
            message += "]";
        }
        
        logger.debug(message);
    }
}

