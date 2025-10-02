package com.kone.kitms.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause.ProblemDetailWithCauseBuilder;

/**
 * KITMS 잘못된 비밀번호 예외 클래스
 * 
 * 이 클래스는 KITMS 시스템에서 잘못된 비밀번호를 입력했을 때 발생하는 예외입니다:
 * - HTTP 400 Bad Request 상태 코드 반환
 * - 비밀번호 오류에 대한 구체적인 오류 메시지 제공
 * - 인증 실패 시 사용되는 예외 클래스
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class InvalidPasswordException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super(
            HttpStatus.BAD_REQUEST,
            ProblemDetailWithCauseBuilder
                .instance()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withType(ErrorConstants.INVALID_PASSWORD_TYPE)
                .withTitle("Incorrect password")
                .build(),
            null
        );
    }
}
