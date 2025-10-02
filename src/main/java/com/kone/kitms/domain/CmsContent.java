package com.kone.kitms.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * CMS 컨텐츠 엔티티
 * 
 * 동적 컨텐츠 관리 시스템(CMS)의 페이지 컨텐츠를 관리하는 엔티티입니다.
 * 정적 HTML 페이지를 데이터베이스 기반의 동적 컨텐츠로 변환하여 관리합니다.
 * 
 * 주요 기능:
 * - 페이지별 컨텐츠 저장 (HTML 형식)
 * - SEO 메타 정보 관리 (title, description, keywords)
 * - 미리보기 기능 지원 (발행 전 미리보기)
 * - 컨텐츠 상태 관리 (ACTIVE/INACTIVE)
 * 
 * 주요 필드:
 * - pageCode: 페이지 식별 코드 (예: sub0101, sub0302)
 * - pageTitle: 페이지 제목
 * - pageContent: 페이지 컨텐츠 (HTML)
 * - metaTitle/metaDescription/metaKeywords: SEO 메타 정보
 * - preview*: 미리보기용 컨텐츠 (발행 전 검토용)
 * 
 * @author KITMS Development Team
 * @version 1.0
 */
@Entity
@Table(name = "cms_content")
public class CmsContent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "page_code", nullable = false, unique = true, length = 50)
    private String pageCode;
    
    @Column(name = "page_title", nullable = false, length = 200)
    private String pageTitle;
    
    @Lob
    @Column(name = "page_content", nullable = false, columnDefinition = "LONGTEXT")
    private String pageContent;
    
    @Column(name = "meta_title", length = 200)
    private String metaTitle;
    
    @Column(name = "meta_description", columnDefinition = "TEXT")
    private String metaDescription;
    
    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;
    
    // 미리보기용 컬럼들
    @Lob
    @Column(name = "preview_content", columnDefinition = "LONGTEXT")
    private String previewContent;
    
    @Column(name = "preview_meta_title", length = 200)
    private String previewMetaTitle;
    
    @Column(name = "preview_meta_description", columnDefinition = "TEXT")
    private String previewMetaDescription;
    
    @Column(name = "preview_meta_keywords", length = 500)
    private String previewMetaKeywords;
    
    @Column(name = "preview_updated_at")
    private LocalDateTime previewUpdatedAt;
    
    @Column(name = "status", length = 20)
    private String status = "ACTIVE";
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 기본 생성자
    public CmsContent() {}
    
    // 생성자
    public CmsContent(String pageCode, String pageTitle, String pageContent) {
        this.pageCode = pageCode;
        this.pageTitle = pageTitle;
        this.pageContent = pageContent;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getter와 Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPageCode() {
        return pageCode;
    }
    
    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }
    
    public String getPageTitle() {
        return pageTitle;
    }
    
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    
    public String getPageContent() {
        return pageContent;
    }
    
    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }
    
    public String getMetaTitle() {
        return metaTitle;
    }
    
    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }
    
    public String getMetaDescription() {
        return metaDescription;
    }
    
    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }
    
    public String getMetaKeywords() {
        return metaKeywords;
    }
    
    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // 미리보기 컬럼들의 Getter와 Setter
    public String getPreviewContent() {
        return previewContent;
    }
    
    public void setPreviewContent(String previewContent) {
        this.previewContent = previewContent;
    }
    
    public String getPreviewMetaTitle() {
        return previewMetaTitle;
    }
    
    public void setPreviewMetaTitle(String previewMetaTitle) {
        this.previewMetaTitle = previewMetaTitle;
    }
    
    public String getPreviewMetaDescription() {
        return previewMetaDescription;
    }
    
    public void setPreviewMetaDescription(String previewMetaDescription) {
        this.previewMetaDescription = previewMetaDescription;
    }
    
    public String getPreviewMetaKeywords() {
        return previewMetaKeywords;
    }
    
    public void setPreviewMetaKeywords(String previewMetaKeywords) {
        this.previewMetaKeywords = previewMetaKeywords;
    }
    
    public LocalDateTime getPreviewUpdatedAt() {
        return previewUpdatedAt;
    }
    
    public void setPreviewUpdatedAt(LocalDateTime previewUpdatedAt) {
        this.previewUpdatedAt = previewUpdatedAt;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
