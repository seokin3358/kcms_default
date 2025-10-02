package com.kone.kitms.security;

import org.springframework.security.core.AuthenticationException;

/**
 * KITMS 비활성화된 사용자 인증 예외 클래스
 * 
 * 이 클래스는 KITMS 시스템에서 비활성화된 사용자가 인증을 시도할 때 발생하는 예외입니다:
 * - 사용자 계정이 비활성화된 상태에서 로그인 시도 시 발생
 * - Spring Security 인증 예외를 상속받아 처리
 * - 사용자 계정 상태 확인 및 보안 강화
 * - 인증 실패 시 적절한 오류 메시지 제공
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public class UserNotActivatedException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserNotActivatedException(String message) {
        super(message);
    }

    public UserNotActivatedException(String message, Throwable t) {
        super(message, t);
    }
}
