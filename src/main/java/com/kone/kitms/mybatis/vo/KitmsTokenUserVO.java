package com.kone.kitms.mybatis.vo;

import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.type.Alias;

/**
 * KITMS 토큰 사용자 VO 클래스
 * 
 * 이 클래스는 KITMS 시스템의 토큰 기반 사용자 정보를 담는 VO입니다:
 * - 사용자 기본 정보 (userNo, userId, userName)
 * - 사용자 인증 정보 (userPwd, authCode)
 * - 사용자 연락처 정보 (userTel, userMobile, userEmail)
 * - 초기 로그인 여부 (firstFlag)
 * - 토큰 기반 인증 및 사용자 관리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Alias("KitmsTokenUserVO")
public class KitmsTokenUserVO {

    private Long userNo;
    private String userId;
    private String userPwd;
    private String userName;
    private String userTel;
    private String userMobile;
    private String userEmail;
    private Boolean firstFlag;

    private String authCode;

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Boolean getFirstFlag() {
        return firstFlag;
    }

    public void setFirstFlag(Boolean firstFlag) {
        this.firstFlag = firstFlag;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return (
            "KitmsTokenUserVO{" +
            "userNo=" +
            userNo +
            ", userId='" +
            userId +
            '\'' +
            ", userPwd='" +
            userPwd +
            '\'' +
            ", userName='" +
            userName +
            '\'' +
            ", userTel='" +
            userTel +
            '\'' +
            ", userMobile='" +
            userMobile +
            '\'' +
            ", userEmail='" +
            userEmail +
            '\'' +
            ", firstFlag=" +
            firstFlag +
            ", authCode='" +
            authCode +
            '}'
        );
    }
}
