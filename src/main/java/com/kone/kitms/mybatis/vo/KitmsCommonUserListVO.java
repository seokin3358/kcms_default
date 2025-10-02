package com.kone.kitms.mybatis.vo;

import java.time.ZonedDateTime;
import org.apache.ibatis.type.Alias;

/**
 * KITMS 공통 사용자 목록 VO 클래스
 * 
 * 이 클래스는 KITMS 시스템의 사용자 목록 조회 결과를 담는 VO입니다:
 * - 사용자 기본 정보 (userNo, userId, userName)
 * - 사용자 권한 정보 (authCode, authValue, authOrder)
 * - 사용자 연락처 정보 (userTel, userMobile, userEmail)
 * - 사용자 상태 정보 (isSorted, isOnVac)
 * - 휴가 정보 (vacStartDate, vacEndDate)
 * - 생성 일시 및 총 개수 정보
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Alias("KitmsCommonUserListVO")
public class KitmsCommonUserListVO {

    String userNo;
    String userId;
    String userName;
    String authCode;
    String authValue;
    String authOrder;
    String createDt;
    String totalCount;
    String userTel;
    String userMobile;
    String userEmail;
    Integer isSorted;
    String vacStartDate;
    String vacEndDate;
    Integer isOnVac;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getAuthOrder() {
        return authOrder;
    }

    public void setAuthOrder(String authOrder) {
        this.authOrder = authOrder;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
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

    public Integer getIsSorted() {
        return isSorted;
    }

    public void setIsSorted(Integer isSorted) {
        this.isSorted = isSorted;
    }

    public Integer getIsOnVac() {
        return isOnVac;
    }

    public void setIsOnVac(Integer isOnVac) {
        this.isOnVac = isOnVac;
    }

    public String getVacStartDate() {
        return vacStartDate;
    }

    public void setVacStartDate(String vacStartDate) {
        this.vacStartDate = vacStartDate;
    }

    public String getVacEndDate() {
        return vacEndDate;
    }

    public void setVacEndDate(String vacEndDate) {
        this.vacEndDate = vacEndDate;
    }
}
