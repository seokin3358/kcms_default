package com.kone.kitms.web.rest;

import com.kone.kitms.domain.CmsContent;
import com.kone.kitms.service.CmsContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * KITMS CMS 컨텐츠 관리 REST API 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 CMS(Content Management System) 컨텐츠 관리를 위한 모든 기능을 제공합니다:
 * - 컨텐츠 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 페이지별 컨텐츠 관리
 * - 미리보기 기능 지원
 * - 컨텐츠 활성화/비활성화 관리
 * - 임시 저장 및 최종 적용 기능
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/cms")
@CrossOrigin(origins = "*")
public class CmsContentResource {
    
    @Autowired
    private CmsContentService cmsContentService;
    
    /**
     * 페이지 코드로 활성화된 컨텐츠 조회
     */
    @GetMapping("/content/{pageCode}")
    public ResponseEntity<CmsContent> getContentByPageCode(@PathVariable String pageCode) {
        Optional<CmsContent> content = cmsContentService.getActiveContentByPageCode(pageCode);
        return content.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 모든 활성화된 컨텐츠 목록 조회
     */
    @GetMapping("/contents")
    public ResponseEntity<List<CmsContent>> getAllActiveContents() {
        List<CmsContent> contents = cmsContentService.getAllActiveContents();
        return ResponseEntity.ok(contents);
    }
    
    /**
     * 컨텐츠 저장 (관리자용)
     */
    @PostMapping("/content")
    public ResponseEntity<CmsContent> saveContent(@RequestBody CmsContent content) {
        CmsContent savedContent = cmsContentService.saveContent(content);
        return ResponseEntity.ok(savedContent);
    }
    
    /**
     * 컨텐츠 업데이트 (관리자용)
     */
    @PutMapping("/content/{id}")
    public ResponseEntity<CmsContent> updateContent(@PathVariable Long id, @RequestBody CmsContent content) {
        content.setId(id);
        CmsContent updatedContent = cmsContentService.updateContent(content);
        return ResponseEntity.ok(updatedContent);
    }
    
    /**
     * 페이지 코드로 컨텐츠 업데이트 (관리자용)
     */
    @PutMapping("/content/page/{pageCode}")
    public ResponseEntity<CmsContent> updateContentByPageCode(@PathVariable String pageCode, @RequestBody CmsContent content) {
        CmsContent updatedContent = cmsContentService.updateContentByPageCode(pageCode, content);
        return ResponseEntity.ok(updatedContent);
    }
    
    /**
     * 컨텐츠 삭제 (상태를 INACTIVE로 변경) (관리자용)
     */
    @DeleteMapping("/content/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        cmsContentService.deleteContent(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 미리보기용 컨텐츠 저장 (임시 저장)
     */
    @PostMapping("/content/preview/{pageCode}")
    public ResponseEntity<CmsContent> savePreviewContent(@PathVariable String pageCode, @RequestBody CmsContent content) {
        CmsContent savedContent = cmsContentService.savePreviewContent(pageCode, content);
        return ResponseEntity.ok(savedContent);
    }
    
    /**
     * 미리보기용 컨텐츠 업데이트 (PUT 메서드)
     */
    @PutMapping("/content/preview/{pageCode}")
    public ResponseEntity<CmsContent> updatePreviewContent(@PathVariable String pageCode, @RequestBody CmsContent content) {
        CmsContent updatedContent = cmsContentService.updatePreviewContent(pageCode, content);
        return ResponseEntity.ok(updatedContent);
    }
    
    /**
     * 미리보기용 컨텐츠 조회
     */
    @GetMapping("/content/preview/{pageCode}")
    public ResponseEntity<CmsContent> getPreviewContent(@PathVariable String pageCode) {
        Optional<CmsContent> content = cmsContentService.getPreviewContentByPageCode(pageCode);
        if (content.isPresent()) {
            return ResponseEntity.ok(content.get());
        } else {
            // 미리보기 컨텐츠가 없으면 실제 컨텐츠를 반환
            Optional<CmsContent> actualContent = cmsContentService.getActiveContentByPageCode(pageCode);
            return actualContent.map(ResponseEntity::ok)
                               .orElse(ResponseEntity.notFound().build());
        }
    }
    
    /**
     * 미리보기 컨텐츠를 실제 컨텐츠로 적용 (최종 저장)
     */
    @PostMapping("/content/apply-preview/{pageCode}")
    public ResponseEntity<CmsContent> applyPreviewContent(@PathVariable String pageCode) {
        CmsContent appliedContent = cmsContentService.applyPreviewContent(pageCode);
        return ResponseEntity.ok(appliedContent);
    }
    
    /**
     * 미리보기 컨텐츠 삭제
     */
    @DeleteMapping("/content/preview/{pageCode}")
    public ResponseEntity<Void> deletePreviewContent(@PathVariable String pageCode) {
        cmsContentService.deletePreviewContent(pageCode);
        return ResponseEntity.ok().build();
    }
}
