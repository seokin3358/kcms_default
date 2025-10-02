package com.kone.kitms.web.rest.vm;

/**
 * KITMS 키 및 비밀번호 뷰 모델 클래스
 * 
 * 이 클래스는 KITMS 시스템의 사용자 키와 비밀번호 정보를 저장하는 뷰 모델입니다:
 * - 비밀번호 재설정을 위한 키 정보 포함
 * - 새로운 비밀번호 필드 포함
 * - 비밀번호 재설정 요청 시 사용되는 데이터 전송 객체
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public class KeyAndPasswordVM {

    private String key;

    private String newPassword;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
