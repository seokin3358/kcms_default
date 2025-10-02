package com.kone.kitms.service;

import com.kone.kitms.domain.CmsContent;
import com.kone.kitms.repository.CmsContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * KITMS CMS 컨텐츠 관리 서비스 클래스
 * 
 * 이 클래스는 KITMS 시스템의 CMS(Content Management System) 컨텐츠 관리를 위한 비즈니스 로직을 제공합니다:
 * - 페이지별 컨텐츠 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 활성화된 컨텐츠 조회 및 관리
 * - 미리보기 기능 지원 (발행 전 검토)
 * - SEO 메타 정보 관리
 * - 컨텐츠 상태 관리 (ACTIVE/INACTIVE)
 * - 미리보기 컨텐츠를 실제 컨텐츠로 적용
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Service
@Transactional
public class CmsContentService {
    
    @Autowired
    private CmsContentRepository cmsContentRepository;
    
    /**
     * 페이지 코드로 활성화된 컨텐츠 조회
     */
    @Transactional(readOnly = true)
    public Optional<CmsContent> getActiveContentByPageCode(String pageCode) {
        return cmsContentRepository.findByPageCodeAndActive(pageCode);
    }
    
    /**
     * 페이지 코드로 컨텐츠 조회 (상태 무관)
     */
    @Transactional(readOnly = true)
    public Optional<CmsContent> getContentByPageCode(String pageCode) {
        return cmsContentRepository.findByPageCode(pageCode);
    }
    
    /**
     * 모든 활성화된 컨텐츠 조회
     */
    @Transactional(readOnly = true)
    public List<CmsContent> getAllActiveContents() {
        return cmsContentRepository.findAll().stream()
                .filter(content -> "ACTIVE".equals(content.getStatus()))
                .toList();
    }
    
    /**
     * 컨텐츠 저장
     */
    public CmsContent saveContent(CmsContent content) {
        return cmsContentRepository.save(content);
    }
    
    /**
     * 컨텐츠 업데이트
     */
    public CmsContent updateContent(CmsContent content) {
        if (content.getId() == null) {
            throw new IllegalArgumentException("컨텐츠 ID가 필요합니다.");
        }
        return cmsContentRepository.save(content);
    }
    
    /**
     * 페이지 코드로 컨텐츠 업데이트
     */
    public CmsContent updateContentByPageCode(String pageCode, CmsContent newContent) {
        Optional<CmsContent> existingContent = cmsContentRepository.findByPageCode(pageCode);
        
        if (existingContent.isPresent()) {
            CmsContent content = existingContent.get();
            
            // 기존 컨텐츠 정보 업데이트
            content.setPageTitle(newContent.getPageTitle());
            content.setPageContent(newContent.getPageContent());
            content.setMetaTitle(newContent.getMetaTitle());
            content.setMetaDescription(newContent.getMetaDescription());
            content.setMetaKeywords(newContent.getMetaKeywords());
            content.setStatus(newContent.getStatus() != null ? newContent.getStatus() : "ACTIVE");
            content.setUpdatedBy(newContent.getUpdatedBy());
            content.setUpdatedAt(java.time.LocalDateTime.now());
            
            return cmsContentRepository.save(content);
        } else {
            // 기존 컨텐츠가 없으면 새로 생성
            newContent.setPageCode(pageCode);
            newContent.setCreatedAt(java.time.LocalDateTime.now());
            newContent.setUpdatedAt(java.time.LocalDateTime.now());
            return cmsContentRepository.save(newContent);
        }
    }
    
    /**
     * 컨텐츠 삭제 (상태를 INACTIVE로 변경)
     */
    public void deleteContent(Long id) {
        Optional<CmsContent> contentOpt = cmsContentRepository.findById(id);
        if (contentOpt.isPresent()) {
            CmsContent content = contentOpt.get();
            content.setStatus("INACTIVE");
            cmsContentRepository.save(content);
        }
    }
    
    /**
     * 페이지 코드 존재 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean existsByPageCode(String pageCode) {
        return cmsContentRepository.existsByPageCode(pageCode);
    }
    
    /**
     * 미리보기용 컨텐츠 업데이트 (PUT 메서드용)
     */
    public CmsContent updatePreviewContent(String pageCode, CmsContent content) {
        Optional<CmsContent> existingContent = cmsContentRepository.findByPageCode(pageCode);
        
        if (existingContent.isPresent()) {
            CmsContent existing = existingContent.get();
            
            // 미리보기 컬럼들에만 업데이트
            existing.setPreviewContent(content.getPreviewContent());
            existing.setPreviewMetaTitle(content.getMetaTitle());
            existing.setPreviewMetaDescription(content.getMetaDescription());
            existing.setPreviewMetaKeywords(content.getMetaKeywords());
            existing.setPreviewUpdatedAt(java.time.LocalDateTime.now());
            
            return cmsContentRepository.save(existing);
        } else {
            throw new IllegalArgumentException("페이지를 찾을 수 없습니다: " + pageCode);
        }
    }
    
    /**
     * 미리보기용 컨텐츠 저장 (임시 저장)
     */
    public CmsContent savePreviewContent(String pageCode, CmsContent content) {
        Optional<CmsContent> existingContent = cmsContentRepository.findByPageCode(pageCode);
        
        if (existingContent.isPresent()) {
            CmsContent existing = existingContent.get();
            
            // 미리보기 컬럼들에만 저장
            existing.setPreviewContent(content.getPageContent());
            existing.setPreviewMetaTitle(content.getMetaTitle());
            existing.setPreviewMetaDescription(content.getMetaDescription());
            existing.setPreviewMetaKeywords(content.getMetaKeywords());
            existing.setPreviewUpdatedAt(java.time.LocalDateTime.now());
            
            return cmsContentRepository.save(existing);
        } else {
            // 기존 컨텐츠가 없으면 새로 생성 (미리보기 컬럼에만 저장)
            CmsContent newContent = new CmsContent();
            newContent.setPageCode(pageCode);
            newContent.setPageTitle(content.getPageTitle());
            newContent.setPreviewContent(content.getPageContent());
            newContent.setPreviewMetaTitle(content.getMetaTitle());
            newContent.setPreviewMetaDescription(content.getMetaDescription());
            newContent.setPreviewMetaKeywords(content.getMetaKeywords());
            newContent.setPreviewUpdatedAt(java.time.LocalDateTime.now());
            newContent.setStatus("ACTIVE");
            newContent.setCreatedAt(java.time.LocalDateTime.now());
            newContent.setUpdatedAt(java.time.LocalDateTime.now());
            
            return cmsContentRepository.save(newContent);
        }
    }
    
    /**
     * 미리보기용 컨텐츠 조회
     */
    @Transactional(readOnly = true)
    public Optional<CmsContent> getPreviewContentByPageCode(String pageCode) {
        Optional<CmsContent> content = cmsContentRepository.findByPageCode(pageCode);
        
        if (content.isPresent()) {
            CmsContent original = content.get();
            
            if (original.getPreviewContent() != null) {
                // 미리보기 컨텐츠가 있으면 미리보기 컬럼들을 실제 컨텐츠로 복사한 객체 반환
                CmsContent previewContent = new CmsContent();
                
                previewContent.setId(original.getId());
                previewContent.setPageCode(original.getPageCode());
                previewContent.setPageTitle(original.getPageTitle());
                previewContent.setPageContent(original.getPreviewContent());
                previewContent.setMetaTitle(original.getPreviewMetaTitle());
                previewContent.setMetaDescription(original.getPreviewMetaDescription());
                previewContent.setMetaKeywords(original.getPreviewMetaKeywords());
                previewContent.setStatus(original.getStatus());
                previewContent.setCreatedBy(original.getCreatedBy());
                previewContent.setCreatedAt(original.getCreatedAt());
                previewContent.setUpdatedBy(original.getUpdatedBy());
                previewContent.setUpdatedAt(original.getPreviewUpdatedAt());
                
                // 미리보기 컨텐츠임을 표시하는 플래그 추가
                previewContent.setMetaDescription("[PREVIEW_CONTENT] " + (original.getPreviewMetaDescription() != null ? original.getPreviewMetaDescription() : ""));
                
                return Optional.of(previewContent);
            } else {
                // 미리보기 컨텐츠가 없으면 실제 컨텐츠를 반환하되 플래그 추가
                CmsContent actualContent = new CmsContent();
                actualContent.setId(original.getId());
                actualContent.setPageCode(original.getPageCode());
                actualContent.setPageTitle(original.getPageTitle());
                actualContent.setPageContent(original.getPageContent());
                actualContent.setMetaTitle(original.getMetaTitle());
                actualContent.setMetaDescription("[ACTUAL_CONTENT] " + (original.getMetaDescription() != null ? original.getMetaDescription() : ""));
                actualContent.setMetaKeywords(original.getMetaKeywords());
                actualContent.setStatus(original.getStatus());
                actualContent.setCreatedBy(original.getCreatedBy());
                actualContent.setCreatedAt(original.getCreatedAt());
                actualContent.setUpdatedBy(original.getUpdatedBy());
                actualContent.setUpdatedAt(original.getUpdatedAt());
                
                return Optional.of(actualContent);
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * 미리보기 컨텐츠를 실제 컨텐츠로 적용 (최종 저장)
     */
    public CmsContent applyPreviewContent(String pageCode) {
        Optional<CmsContent> contentOpt = cmsContentRepository.findByPageCode(pageCode);
        
        if (contentOpt.isPresent()) {
            CmsContent content = contentOpt.get();
            
            if (content.getPreviewContent() != null) {
                // 미리보기 컨텐츠를 실제 컨텐츠로 복사
                content.setPageContent(content.getPreviewContent());
                content.setMetaTitle(content.getPreviewMetaTitle());
                content.setMetaDescription(content.getPreviewMetaDescription());
                content.setMetaKeywords(content.getPreviewMetaKeywords());
                content.setUpdatedAt(java.time.LocalDateTime.now());
                
                // 미리보기 컨텐츠 삭제
                content.setPreviewContent(null);
                content.setPreviewMetaTitle(null);
                content.setPreviewMetaDescription(null);
                content.setPreviewMetaKeywords(null);
                content.setPreviewUpdatedAt(null);
                
                return cmsContentRepository.save(content);
            } else {
                throw new IllegalArgumentException("적용할 미리보기 컨텐츠가 없습니다.");
            }
        } else {
            throw new IllegalArgumentException("페이지를 찾을 수 없습니다: " + pageCode);
        }
    }
    
    /**
     * 미리보기 컨텐츠 삭제
     */
    public void deletePreviewContent(String pageCode) {
        Optional<CmsContent> contentOpt = cmsContentRepository.findByPageCode(pageCode);
        
        if (contentOpt.isPresent()) {
            CmsContent content = contentOpt.get();
            
            // 미리보기 컨텐츠만 삭제
            content.setPreviewContent(null);
            content.setPreviewMetaTitle(null);
            content.setPreviewMetaDescription(null);
            content.setPreviewMetaKeywords(null);
            content.setPreviewUpdatedAt(null);
            
            cmsContentRepository.save(content);
        }
    }
}
