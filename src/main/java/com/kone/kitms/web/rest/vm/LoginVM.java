package com.kone.kitms.web.rest.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * KITMS 로그인 뷰 모델 클래스
 * 
 * 이 클래스는 KITMS 시스템의 사용자 로그인 정보를 저장하는 뷰 모델입니다:
 * - 사용자명과 비밀번호 필드 포함
 * - 로그인 상태 유지 옵션 제공
 * - 입력 유효성 검사 어노테이션 적용
 * - 로그인 요청 시 사용되는 데이터 전송 객체
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public class LoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    private boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginVM{" +
            "username='" + username + '\'' +
            ", rememberMe=" + rememberMe +
            '}';
    }
}
