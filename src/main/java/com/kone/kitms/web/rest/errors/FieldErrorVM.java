package com.kone.kitms.web.rest.errors;

import java.io.Serializable;

/**
 * KITMS 필드 오류 뷰 모델 클래스
 * 
 * 이 클래스는 KITMS 시스템의 필드 유효성 검사 오류 정보를 저장하는 뷰 모델입니다:
 * - 객체 이름, 필드명, 오류 메시지 정보 포함
 * - 유효성 검사 실패 시 사용되는 데이터 전송 객체
 * - 직렬화 가능한 구조로 설계
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String objectName;

    private final String field;

    private final String message;

    public FieldErrorVM(String dto, String field, String message) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
