package com.kone.kitms.config;

/**
 * KITMS 애플리케이션 상수 클래스
 * 
 * 이 클래스는 KITMS 시스템의 애플리케이션 전반에서 사용되는 상수들을 정의합니다:
 * - 로그인 정규식 패턴
 * - 시스템 사용자 식별자
 * - 기본 언어 설정
 * - 애플리케이션 전역 상수
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "ko";

    private Constants() {}
}
