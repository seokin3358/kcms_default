package com.kone.kitms.web.rest.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * KITMS 로그인 뷰 모델 클래스
 * 
 * 이 클래스는 KITMS 시스템의 사용자 로그인 정보를 저장하는 뷰 모델입니다:
 * - 사용자 ID와 비밀번호 필드 포함
 * - 입력 유효성 검사 어노테이션 적용
 * - 로그인 요청 시 사용되는 데이터 전송 객체
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public class KitmsLoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String userId;

    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginVM{" +
            "userId='" + userId + '\'' +
            '}';
    }
}
