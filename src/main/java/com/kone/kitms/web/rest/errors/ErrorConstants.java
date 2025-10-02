package com.kone.kitms.web.rest.errors;

import java.net.URI;

/**
 * KITMS 오류 상수 클래스
 * 
 * 이 클래스는 KITMS 시스템의 오류 처리에 사용되는 상수들을 정의합니다:
 * - 오류 메시지 키 상수
 * - 문제 세부사항 URL 상수
 * - 다양한 오류 타입에 대한 URI 상수
 * - 유효성 검사, 동시성 실패, 비밀번호 오류 등
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");

    private ErrorConstants() {}
}
