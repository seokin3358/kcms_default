package com.kone.kitms.repository;

import com.kone.kitms.domain.AdminMenu;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 어드민 메뉴 리포지토리
 */
@Repository
public interface AdminMenuRepository extends JpaRepository<AdminMenu, Long> {

    /**
     * 활성화된 모든 메뉴 조회 (정렬순)
     */
    @Query("SELECT m FROM AdminMenu m WHERE m.isActive = true AND m.isVisible = true ORDER BY m.menuOrder ASC")
    List<AdminMenu> findActiveMenusOrderByOrder();

    /**
     * 메뉴명으로 검색
     */
    @Query("SELECT m FROM AdminMenu m WHERE m.menuName LIKE %:menuName% AND m.isActive = true ORDER BY m.menuOrder ASC")
    List<AdminMenu> findByMenuNameContaining(@Param("menuName") String menuName);

    /**
     * URL로 메뉴 조회
     */
    @Query("SELECT m FROM AdminMenu m WHERE m.menuUrl = :menuUrl AND m.isActive = true")
    AdminMenu findByMenuUrl(@Param("menuUrl") String menuUrl);

    /**
     * 활성 메뉴 개수 조회
     */
    @Query("SELECT COUNT(m) FROM AdminMenu m WHERE m.isActive = true")
    long countActiveMenus();

    /**
     * 활성화되고 표시되는 메뉴 목록 조회 (정렬순)
     */
    List<AdminMenu> findByIsActiveTrueAndIsVisibleTrueOrderByMenuOrder();

    /**
     * 메뉴명으로 활성 메뉴 조회
     */
    Optional<AdminMenu> findByMenuNameAndIsActiveTrue(String menuName);
}
