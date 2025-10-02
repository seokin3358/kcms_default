package com.kone.kitms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * KITMS 사용자 엔티티
 * 
 * 시스템 사용자 정보를 관리하는 엔티티입니다.
 * 사용자의 기본 정보, 권한, 연락처, 휴가 정보 등을 포함합니다.
 * 
 * 주요 필드:
 * - userId: 사용자 ID (로그인용)
 * - userPwd: 암호화된 비밀번호
 * - userName: 사용자 이름
 * - authCode: 권한 코드 (001: 메인관리자, 002: 서브관리자)
 * - enable: 계정 활성화 여부
 * - organNo: 소속 조직 번호
 * 
 * @author KITMS Development Team
 * @version 1.0
 */
@Entity
@Table(name = "kitms_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KitmsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;

    @NotNull
    @Size(min = 3, max = 30)
    @Column(name = "user_id", length = 30, nullable = false, unique = true)
    private String userId;

    @NotNull
    @Size(max = 512)
    @Column(name = "user_pwd", length = 512, nullable = false)
    private String userPwd;

    @NotNull
    @Size(max = 30)
    @Column(name = "user_name", length = 30, nullable = false)
    private String userName;

    @NotNull
    @Size(max = 3)
    @Column(name = "auth_code", length = 3, nullable = false)
    private String authCode;

    @Size(max = 15)
    @Column(name = "user_tel", length = 15)
    private String userTel;

    @Size(max = 15)
    @Column(name = "user_mobile", length = 15)
    private String userMobile;

    @Size(max = 50)
    @Column(name = "user_email", length = 50)
    private String userEmail;

    @NotNull
    @Column(name = "enable", nullable = false)
    private Boolean enable;

    @Column(name = "vac_start_date")
    private LocalDate vacStartDate;

    @Column(name = "vac_end_date")
    private LocalDate vacEndDate;

    @Size(max = 500)
    @Column(name = "user_etc", length = 500)
    private String userEtc;

    @Column(name = "create_dt")
    private ZonedDateTime createDt;

    @Size(max = 30)
    @Column(name = "create_user_id", length = 30)
    private String createUserId;

    @NotNull
    @Column(name = "first_flag", nullable = false)
    private Boolean firstFlag;

    @Column(name = "last_pass_mod_dt")
    private ZonedDateTime lastPassModDt;

    @Column(name = "onsite_branch_no")
    private Long onsiteBranchNo;

    @Size(max = 10)
    @Column(name = "user_role", length = 10)
    private String userRole;

    @NotNull
    @Column(name = "organ_no", nullable = false)
    private Long organNo;

    public Long getOnsiteBranchNo() {
        return this.onsiteBranchNo;
    }

    public void setOnsiteBranchNo(Long onsiteBranchNo) {
        this.onsiteBranchNo = onsiteBranchNo;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Long getUserNo() {
        return this.userNo;
    }

    public KitmsUser userNo(Long userNo) {
        this.setUserNo(userNo);
        return this;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return this.userId;
    }

    public KitmsUser userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return this.userPwd;
    }

    public KitmsUser userPwd(String userPwd) {
        this.setUserPwd(userPwd);
        return this;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return this.userName;
    }

    public KitmsUser userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return this.userTel;
    }

    public KitmsUser userTel(String userTel) {
        this.setUserTel(userTel);
        return this;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserMobile() {
        return this.userMobile;
    }

    public KitmsUser userMobile(String userMobile) {
        this.setUserMobile(userMobile);
        return this;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public KitmsUser userEmail(String userEmail) {
        this.setUserEmail(userEmail);
        return this;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public KitmsUser enable(Boolean enable) {
        this.setEnable(enable);
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public LocalDate getVacStartDate() {
        return this.vacStartDate;
    }

    public KitmsUser vacStartDate(LocalDate vacStartDate) {
        this.setVacStartDate(vacStartDate);
        return this;
    }

    public void setVacStartDate(LocalDate vacStartDate) {
        this.vacStartDate = vacStartDate;
    }

    public LocalDate getVacEndDate() {
        return this.vacEndDate;
    }

    public KitmsUser vacEndDate(LocalDate vacEndDate) {
        this.setVacEndDate(vacEndDate);
        return this;
    }

    public void setVacEndDate(LocalDate vacEndDate) {
        this.vacEndDate = vacEndDate;
    }

    public String getUserEtc() {
        return this.userEtc;
    }

    public KitmsUser userEtc(String userEtc) {
        this.setUserEtc(userEtc);
        return this;
    }

    public void setUserEtc(String userEtc) {
        this.userEtc = userEtc;
    }

    public ZonedDateTime getCreateDt() {
        return this.createDt;
    }

    public KitmsUser createDt(ZonedDateTime createDt) {
        this.setCreateDt(createDt);
        return this;
    }

    public void setCreateDt(ZonedDateTime createDt) {
        this.createDt = createDt;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public KitmsUser createUserId(String createUserId) {
        this.setCreateUserId(createUserId);
        return this;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Boolean getFirstFlag() {
        return this.firstFlag;
    }

    public KitmsUser firstFlag(Boolean firstFlag) {
        this.setFirstFlag(firstFlag);
        return this;
    }

    public void setFirstFlag(Boolean firstFlag) {
        this.firstFlag = firstFlag;
    }

    public ZonedDateTime getLastPassModDt() {
        return this.lastPassModDt;
    }

    public KitmsUser lastPassModDt(ZonedDateTime lastPassModDt) {
        this.setLastPassModDt(lastPassModDt);
        return this;
    }

    public void setLastPassModDt(ZonedDateTime lastPassModDt) {
        this.lastPassModDt = lastPassModDt;
    }

    public Long getOrganNo() {
        return organNo;
    }

    public void setOrganNo(Long organNo) {
        this.organNo = organNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KitmsUser)) {
            return false;
        }
        return getUserNo() != null && getUserNo().equals(((KitmsUser) o).getUserNo());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "KitmsUser{" +
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
            ", authCode='" +
            authCode +
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
            ", enable=" +
            enable +
            ", vacStartDate=" +
            vacStartDate +
            ", vacEndDate=" +
            vacEndDate +
            ", userEtc='" +
            userEtc +
            '\'' +
            ", createDt=" +
            createDt +
            ", createUserId='" +
            createUserId +
            '\'' +
            ", firstFlag=" +
            firstFlag +
            ", lastPassModDt=" +
            lastPassModDt +
            ", onsiteBranchNo=" +
            onsiteBranchNo +
            ", userRole='" +
            userRole +
            '\'' +
            ", organNo=" +
            organNo +
            '}'
        );
    }
}
