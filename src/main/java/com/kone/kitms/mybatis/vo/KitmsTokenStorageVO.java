package com.kone.kitms.mybatis.vo;

import org.apache.ibatis.type.Alias;

/**
 * KITMS 토큰 저장소 VO 클래스
 * 
 * 이 클래스는 KITMS 시스템의 토큰 저장소 정보를 담는 VO입니다:
 * - 사용자 ID (userId)
 * - 사용자 토큰 (userToken)
 * - 토큰 기반 인증 및 세션 관리
 * - 사용자별 토큰 정보 저장
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Alias("KitmsTokenStorageVO")
public class KitmsTokenStorageVO {

    String userId;
    String userToken;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
