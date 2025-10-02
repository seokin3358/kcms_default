package com.kone.kitms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * KITMS 공지사항 엔티티
 * 
 * 시스템 공지사항 정보를 관리하는 엔티티입니다.
 * 관리자가 작성한 공지사항의 제목, 내용, 작성자 정보 등을 저장합니다.
 * 
 * 주요 필드:
 * - noticeTitle: 공지사항 제목
 * - noticeContent: 공지사항 내용 (HTML 형식 지원)
 * - createUserId: 작성자 ID
 * - createDt: 작성일시
 * - staticFlag: 고정 공지 여부 (true: 상단 고정, false: 일반)
 * 
 * @author KITMS Development Team
 * @version 1.0
 */
@Entity
@Table(name = "kitms_notice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KitmsNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_no")
    private Long noticeNo;

    @NotNull
    @Size(max = 100)
    @Column(name = "notice_title", length = 100, nullable = false)
    private String noticeTitle;

    @NotNull
    @Lob
    @Column(name = "notice_content", nullable = false)
    private String noticeContent;

    @Column(name = "create_dt", nullable = false)
    private ZonedDateTime createDt;

    @Size(max = 30)
    @Column(name = "create_user_id", length = 30, nullable = false)
    private String createUserId;

    @NotNull
    @Column(name = "static_flag", nullable = false)
    private Boolean staticFlag;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getNoticeNo() {
        return this.noticeNo;
    }

    public KitmsNotice noticeNo(Long noticeNo) {
        this.setNoticeNo(noticeNo);
        return this;
    }

    public void setNoticeNo(Long noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getNoticeTitle() {
        return this.noticeTitle;
    }

    public KitmsNotice noticeTitle(String noticeTitle) {
        this.setNoticeTitle(noticeTitle);
        return this;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return this.noticeContent;
    }

    public KitmsNotice noticeContent(String noticeContent) {
        this.setNoticeContent(noticeContent);
        return this;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public ZonedDateTime getCreateDt() {
        return this.createDt;
    }

    public KitmsNotice createDt(ZonedDateTime createDt) {
        this.setCreateDt(createDt);
        return this;
    }

    public void setCreateDt(ZonedDateTime createDt) {
        this.createDt = createDt;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public KitmsNotice createUserId(String createUserId) {
        this.setCreateUserId(createUserId);
        return this;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Boolean getStaticFlag() {
        return this.staticFlag;
    }

    public KitmsNotice staticFlag(Boolean staticFlag) {
        this.setStaticFlag(staticFlag);
        return this;
    }

    public void setStaticFlag(Boolean staticFlag) {
        this.staticFlag = staticFlag;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KitmsNotice)) {
            return false;
        }
        return getNoticeNo() != null && getNoticeNo().equals(((KitmsNotice) o).getNoticeNo());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KitmsNotice{" +
            "noticeNo=" + getNoticeNo() +
            ", noticeTitle='" + getNoticeTitle() + "'" +
            ", noticeContent='" + getNoticeContent() + "'" +
            ", createDt='" + getCreateDt() + "'" +
            ", createUserId='" + getCreateUserId() + "'" +
            ", staticFlag='" + getStaticFlag() + "'" +
            "}";
    }
}
