package com.kone.kitms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * KITMS 뉴스룸(보도자료) 엔티티
 * 
 * 회사의 보도자료 및 뉴스룸 정보를 관리하는 엔티티입니다.
 * 외부 언론사나 뉴스 사이트에 게시된 보도자료 링크를 관리합니다.
 * 
 * 주요 기능:
 * - 보도자료 제목 및 URL 관리
 * - 보도자료 이미지 관리
 * - 보도자료 상태 관리 (ACTIVE/INACTIVE)
 * - 작성자 및 수정 이력 관리
 * 
 * 주요 필드:
 * - newsroomTitle: 보도자료 제목
 * - newsroomUrl: 보도자료 링크 URL
 * - newsroomImage: 보도자료 관련 이미지
 * - newsroomStatus: 보도자료 상태 (ACTIVE/INACTIVE)
 * 
 * @author KITMS Development Team
 * @version 1.0
 */
@Entity
@Table(name = "kitms_newsroom")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class KitmsNewsroom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newsroom_no")
    private Long newsroomNo;

    @NotNull
    @Size(max = 200)
    @Column(name = "newsroom_title", length = 200, nullable = false)
    private String newsroomTitle;

    @NotNull
    @Size(max = 500)
    @Column(name = "newsroom_url", length = 500, nullable = false)
    private String newsroomUrl;

    @Size(max = 500)
    @Column(name = "newsroom_image", length = 500)
    private String newsroomImage;

    @Column(name = "newsroom_status", length = 20)
    private String newsroomStatus = "ACTIVE";

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // Constructors
    public KitmsNewsroom() {}

    public KitmsNewsroom(String newsroomTitle, String newsroomUrl) {
        this.newsroomTitle = newsroomTitle;
        this.newsroomUrl = newsroomUrl;
    }

    // Getters and Setters
    public Long getNewsroomNo() {
        return newsroomNo;
    }

    public void setNewsroomNo(Long newsroomNo) {
        this.newsroomNo = newsroomNo;
    }

    public String getNewsroomTitle() {
        return newsroomTitle;
    }

    public void setNewsroomTitle(String newsroomTitle) {
        this.newsroomTitle = newsroomTitle;
    }

    public String getNewsroomUrl() {
        return newsroomUrl;
    }

    public void setNewsroomUrl(String newsroomUrl) {
        this.newsroomUrl = newsroomUrl;
    }

    public String getNewsroomImage() {
        return newsroomImage;
    }

    public void setNewsroomImage(String newsroomImage) {
        this.newsroomImage = newsroomImage;
    }

    public String getNewsroomStatus() {
        return newsroomStatus;
    }

    public void setNewsroomStatus(String newsroomStatus) {
        this.newsroomStatus = newsroomStatus;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KitmsNewsroom)) {
            return false;
        }
        return newsroomNo != null && newsroomNo.equals(((KitmsNewsroom) o).newsroomNo);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "KitmsNewsroom{" +
                "newsroomNo=" + newsroomNo +
                ", newsroomTitle='" + newsroomTitle + '\'' +
                ", newsroomUrl='" + newsroomUrl + '\'' +
                ", newsroomImage='" + newsroomImage + '\'' +
                ", newsroomStatus='" + newsroomStatus + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
