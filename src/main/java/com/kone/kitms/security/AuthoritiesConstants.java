package com.kone.kitms.security;

/**
 * KITMS Spring Security 권한 상수 클래스
 * 
 * 이 클래스는 KITMS 시스템의 Spring Security 권한에 사용되는 상수들을 정의합니다:
 * - 관리자 권한 (ROLE_ADMIN)
 * - 일반 사용자 권한 (ROLE_USER)
 * - 익명 사용자 권한 (ROLE_ANONYMOUS)
 * - 권한 기반 접근 제어에 사용
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
