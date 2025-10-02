package com.kone.kitms.mapper;

import com.kone.kitms.domain.KitmsMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * KITMS 메뉴 매퍼 인터페이스
 * 
 * 이 인터페이스는 KITMS 시스템의 메뉴 관리를 위한 MyBatis 매퍼입니다:
 * - 메뉴 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 활성 메뉴 조회 및 관리
 * - 메뉴 레벨별 조회
 * - 부모-자식 메뉴 관계 조회
 * - 메뉴 상태 및 정렬 순서 관리
 * - 재귀 쿼리를 통한 메뉴 트리 구조 조회
 * - 계층적 메뉴 구조 지원
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Mapper
public interface KitmsMenuMapper {

    /**
     * 모든 활성 메뉴 조회
     */
    List<KitmsMenu> findAllActiveMenus();

    /**
     * 모든 메뉴 조회 (관리자용)
     */
    List<KitmsMenu> findAllMenus();

    /**
     * 메뉴 레벨로 조회
     */
    List<KitmsMenu> findByMenuLevel(@Param("menuLevel") Integer menuLevel);

    /**
     * 부모 ID로 자식 메뉴 조회
     */
    List<KitmsMenu> findByParentId(@Param("parentId") Long parentId);

    /**
     * 메뉴 ID로 조회
     */
    KitmsMenu findById(@Param("menuId") Long menuId);

    /**
     * 메뉴 생성
     */
    int insert(KitmsMenu menu);

    /**
     * 메뉴 수정
     */
    int update(KitmsMenu menu);

    /**
     * 메뉴 삭제
     */
    int deleteById(@Param("menuId") Long menuId);

    /**
     * 메뉴 상태 변경
     */
    int updateStatus(@Param("menuId") Long menuId, @Param("isActive") String isActive);

    /**
     * 메뉴 정렬 순서 변경
     */
    int updateSortOrder(@Param("menuId") Long menuId, @Param("sortOrder") Integer sortOrder);

    /**
     * 전체 메뉴 트리 구조 조회 (WITH RECURSIVE 사용)
     */
    List<KitmsMenu> findMenuTreeWithRecursive();

    /**
     * 특정 메뉴와 모든 하위 메뉴 조회
     */
    List<KitmsMenu> findMenuWithAllChildren(@Param("menuId") Long menuId);
}
