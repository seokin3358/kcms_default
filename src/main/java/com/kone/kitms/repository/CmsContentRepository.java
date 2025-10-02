package com.kone.kitms.repository;

import com.kone.kitms.domain.CmsContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CMS 컨텐츠 Repository
 */
@Repository
public interface CmsContentRepository extends JpaRepository<CmsContent, Long> {
    
    /**
     * 페이지 코드로 활성화된 컨텐츠 조회
     */
    @Query("SELECT c FROM CmsContent c WHERE c.pageCode = :pageCode AND c.status = 'ACTIVE'")
    Optional<CmsContent> findByPageCodeAndActive(@Param("pageCode") String pageCode);
    
    /**
     * 페이지 코드로 컨텐츠 조회 (상태 무관)
     */
    Optional<CmsContent> findByPageCode(String pageCode);
    
    /**
     * 페이지 코드 존재 여부 확인
     */
    boolean existsByPageCode(String pageCode);
}
