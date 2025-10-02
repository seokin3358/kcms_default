package com.kone.kitms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * KITMS 로그인 로그 엔티티 클래스
 * 
 * 이 클래스는 KITMS 시스템의 사용자 로그인 로그 정보를 저장하는 엔티티입니다:
 * - 사용자 로그인 성공/실패 기록
 * - 로그인 시간 및 IP 주소 추적
 * - 로그인 실패 사유 기록
 * - 보안 모니터링 및 감사 추적
 * - JPA 엔티티로 데이터베이스 매핑
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "kitms_login_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KitmsLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_no")
    private Long logNo;

    @Size(max = 30)
    @Column(name = "user_id", length = 30, nullable = false)
    private String userId;

    @Column(name = "login_success")
    private Boolean loginSuccess;

    @Column(name = "login_dt", nullable = false)
    private ZonedDateTime loginDt;

    @Size(max = 20)
    @Column(name = "login_ip", length = 20)
    private String loginIp;

    @Size(max = 100)
    @Column(name = "login_reason", length = 100)
    private String loginReason;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getLogNo() {
        return this.logNo;
    }

    public KitmsLoginLog logNo(Long logNo) {
        this.setLogNo(logNo);
        return this;
    }

    public void setLogNo(Long logNo) {
        this.logNo = logNo;
    }

    public String getUserId() {
        return this.userId;
    }

    public KitmsLoginLog userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getLoginSuccess() {
        return this.loginSuccess;
    }

    public KitmsLoginLog loginSuccess(Boolean loginSuccess) {
        this.setLoginSuccess(loginSuccess);
        return this;
    }

    public void setLoginSuccess(Boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public ZonedDateTime getLoginDt() {
        return this.loginDt;
    }

    public KitmsLoginLog loginDt(ZonedDateTime loginDt) {
        this.setLoginDt(loginDt);
        return this;
    }

    public void setLoginDt(ZonedDateTime loginDt) {
        this.loginDt = loginDt;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public KitmsLoginLog loginIp(String loginIp) {
        this.setLoginIp(loginIp);
        return this;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginReason() {
        return this.loginReason;
    }

    public KitmsLoginLog loginReason(String loginReason) {
        this.setLoginReason(loginReason);
        return this;
    }

    public void setLoginReason(String loginReason) {
        this.loginReason = loginReason;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KitmsLoginLog)) {
            return false;
        }
        return getLogNo() != null && getLogNo().equals(((KitmsLoginLog) o).getLogNo());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KitmsLoginLog{" +
            "logNo=" + getLogNo() +
            ", userId='" + getUserId() + "'" +
            ", loginSuccess='" + getLoginSuccess() + "'" +
            ", loginDt='" + getLoginDt() + "'" +
            ", loginIp='" + getLoginIp() + "'" +
            ", loginReason='" + getLoginReason() + "'" +
            "}";
    }
}
