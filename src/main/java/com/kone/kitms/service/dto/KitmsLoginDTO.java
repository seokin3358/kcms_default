package com.kone.kitms.service.dto;

import com.kone.kitms.domain.KitmsUser;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

import lombok.Builder;

@Builder
public class KitmsLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userNo;

    @NotNull
    private String userId;

    @NotNull
    private String userPwd;

    @NotNull
    private String userName;

    private String userTel;

    private String userMobile;

    private String userEmail;

    private Boolean firstFlag;

    @NotNull
    private String authCode;

    private String authValue;

    public KitmsLoginDTO() {}

    public KitmsLoginDTO(KitmsUser kitmsUser, String authValue) {
        this.userNo = kitmsUser.getUserNo();
        this.userId = kitmsUser.getUserId();
        this.userPwd = kitmsUser.getUserPwd();
        this.userName = kitmsUser.getUserName();
        this.userTel = kitmsUser.getUserTel();
        this.userMobile = kitmsUser.getUserMobile();
        this.userEmail = kitmsUser.getUserEmail();
        this.firstFlag = kitmsUser.getFirstFlag();
        this.authCode = kitmsUser.getAuthCode();
        this.authValue = authValue;
    }

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

    public String getAuthValue() {
        return authValue;
    }

    public void setAuthValue(String authValue) {
        this.authValue = authValue;
    }

    @Override
    public String toString() {
        return (
            "KitmsUserDTO{" +
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
            '\'' +
            ", authValue='" +
            authValue +
            '}'
        );
    }
}
