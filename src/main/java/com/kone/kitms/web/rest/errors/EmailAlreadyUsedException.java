package com.kone.kitms.web.rest.errors;

/**
 * KITMS 이메일 중복 사용 예외 클래스
 * 
 * 이 클래스는 KITMS 시스템에서 이미 사용 중인 이메일로 회원가입을 시도할 때 발생하는 예외입니다:
 * - HTTP 400 Bad Request 상태 코드 반환
 * - 이메일 중복 사용에 대한 구체적인 오류 메시지 제공
 * - 사용자 관리 관련 오류로 분류
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }
}
