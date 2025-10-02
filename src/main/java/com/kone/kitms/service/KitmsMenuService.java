package com.kone.kitms.service;

import com.kone.kitms.domain.KitmsMenu;
import com.kone.kitms.mapper.KitmsMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * KITMS 메뉴 관리 서비스
 * 
 * 이 클래스는 KITMS 시스템의 메뉴 관리를 위한 비즈니스 로직을 제공합니다:
 * - 계층적 메뉴 트리 구조 관리
 * - 메뉴 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 메뉴 활성화/비활성화 관리
 * - 메뉴 정렬 순서 관리
 * - 메뉴 경로 생성 및 관리
 * - JSON 형태의 메뉴 트리 변환
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Service
public class KitmsMenuService {

    @Autowired
    private KitmsMenuMapper kitmsMenuMapper;

    /**
     * 전체 메뉴 트리 구조 조회
     */
    public List<KitmsMenu> getAllMenuTree() {
        List<KitmsMenu> allMenus = kitmsMenuMapper.findAllActiveMenus();
        return buildMenuTree(allMenus);
    }

    /**
     * 1depth 메뉴만 조회
     */
    public List<KitmsMenu> getRootMenus() {
        return kitmsMenuMapper.findByMenuLevel(1);
    }

    /**
     * 특정 부모 메뉴의 자식 메뉴 조회
     */
    public List<KitmsMenu> getChildMenus(Long parentId) {
        return kitmsMenuMapper.findByParentId(parentId);
    }

    /**
     * 메뉴 ID로 메뉴 조회
     */
    public KitmsMenu getMenuById(Long menuId) {
        return kitmsMenuMapper.findById(menuId);
    }

    /**
     * 메뉴 생성
     */
    public int createMenu(KitmsMenu menu) {
        // 기본값 설정
        if (menu.getIsActive() == null) {
            menu.setIsActive("Y");
        }
        if (menu.getIsNewWindow() == null) {
            menu.setIsNewWindow("N");
        }
        if (menu.getSortOrder() == null) {
            menu.setSortOrder(0);
        }
        if (menu.getCreatedAt() == null) {
            menu.setCreatedAt(java.time.LocalDateTime.now());
        }
        if (menu.getUpdatedAt() == null) {
            menu.setUpdatedAt(java.time.LocalDateTime.now());
        }
        if (menu.getCreatedBy() == null) {
            menu.setCreatedBy("admin");
        }
        if (menu.getUpdatedBy() == null) {
            menu.setUpdatedBy("admin");
        }
        
        return kitmsMenuMapper.insert(menu);
    }

    /**
     * 메뉴 수정
     */
    public int updateMenu(KitmsMenu menu) {
        // 수정 시간과 수정자 설정
        menu.setUpdatedAt(java.time.LocalDateTime.now());
        if (menu.getUpdatedBy() == null) {
            menu.setUpdatedBy("admin");
        }
        
        // is_new_window 필드가 null인 경우 기본값 설정
        if (menu.getIsNewWindow() == null) {
            menu.setIsNewWindow("N");
        }
        
        return kitmsMenuMapper.update(menu);
    }

    /**
     * 메뉴 삭제 (논리 삭제)
     */
    public int deleteMenu(Long menuId) {
        return kitmsMenuMapper.deleteById(menuId);
    }

    /**
     * 메뉴 활성화/비활성화
     */
    public int toggleMenuStatus(Long menuId, String isActive) {
        return kitmsMenuMapper.updateStatus(menuId, isActive);
    }

    /**
     * 메뉴 정렬 순서 변경
     */
    public int updateMenuSortOrder(Long menuId, Integer sortOrder) {
        return kitmsMenuMapper.updateSortOrder(menuId, sortOrder);
    }

    /**
     * 전체 메뉴 목록 조회 (관리자용)
     */
    public List<KitmsMenu> getAllMenus() {
        return kitmsMenuMapper.findAllMenus();
    }

    /**
     * 메뉴 트리 구조 생성
     */
    private List<KitmsMenu> buildMenuTree(List<KitmsMenu> allMenus) {
        Map<Long, KitmsMenu> menuMap = new HashMap<>();
        List<KitmsMenu> rootMenus = new ArrayList<>();

        // 활성화된 메뉴만 Map에 저장
        for (KitmsMenu menu : allMenus) {
            if ("Y".equals(menu.getIsActive())) {
                menuMap.put(menu.getMenuId(), menu);
            }
        }

        // 부모-자식 관계 설정 (활성화된 메뉴만)
        for (KitmsMenu menu : allMenus) {
            if ("Y".equals(menu.getIsActive())) {
                if (menu.getParentId() == null) {
                    // 루트 메뉴
                    rootMenus.add(menu);
                } else {
                    // 자식 메뉴
                    KitmsMenu parent = menuMap.get(menu.getParentId());
                    if (parent != null) {
                        parent.addChild(menu);
                    }
                }
            }
        }

        // 정렬
        sortMenus(rootMenus);
        return rootMenus;
    }

    /**
     * 메뉴 정렬
     */
    private void sortMenus(List<KitmsMenu> menus) {
        if (menus == null || menus.isEmpty()) {
            return;
        }

        // sortOrder로 정렬
        menus.sort((m1, m2) -> {
            if (m1.getSortOrder() == null) return 1;
            if (m2.getSortOrder() == null) return -1;
            return m1.getSortOrder().compareTo(m2.getSortOrder());
        });

        // 자식 메뉴들도 재귀적으로 정렬
        for (KitmsMenu menu : menus) {
            if (menu.hasChildren()) {
                sortMenus(menu.getChildren());
            }
        }
    }

    /**
     * 메뉴 경로 생성 (예: 케이원소개 > 회사소개)
     */
    public String generateMenuPath(KitmsMenu menu) {
        List<String> pathParts = new ArrayList<>();
        KitmsMenu current = menu;

        while (current != null) {
            pathParts.add(0, current.getMenuName());
            current = current.getParent();
        }

        return String.join(" > ", pathParts);
    }

    /**
     * 특정 메뉴와 모든 하위 메뉴 조회
     */
    public List<KitmsMenu> getMenuWithAllChildren(Long menuId) {
        List<KitmsMenu> allMenus = kitmsMenuMapper.findAllActiveMenus();
        List<KitmsMenu> result = new ArrayList<>();
        
        // 특정 메뉴 찾기
        KitmsMenu targetMenu = allMenus.stream()
                .filter(menu -> menu.getMenuId().equals(menuId))
                .findFirst()
                .orElse(null);

        if (targetMenu != null) {
            result.add(targetMenu);
            addAllChildren(targetMenu, allMenus, result);
        }

        return result;
    }

    /**
     * 재귀적으로 모든 자식 메뉴 추가
     */
    private void addAllChildren(KitmsMenu parent, List<KitmsMenu> allMenus, List<KitmsMenu> result) {
        List<KitmsMenu> children = allMenus.stream()
                .filter(menu -> parent.getMenuId().equals(menu.getParentId()))
                .collect(Collectors.toList());

        for (KitmsMenu child : children) {
            result.add(child);
            addAllChildren(child, allMenus, result);
        }
    }

    /**
     * 메뉴 계층 구조를 JSON 형태로 변환
     */
    public List<Map<String, Object>> getMenuTreeAsJson() {
        List<KitmsMenu> menuTree = getAllMenuTree();
        return convertToJson(menuTree);
    }

    /**
     * 메뉴 트리를 JSON 형태로 변환
     */
    private List<Map<String, Object>> convertToJson(List<KitmsMenu> menus) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (KitmsMenu menu : menus) {
            Map<String, Object> menuMap = new HashMap<>();
            menuMap.put("menuId", menu.getMenuId());
            menuMap.put("menuName", menu.getMenuName());
            menuMap.put("menuUrl", menu.getMenuUrl());
            menuMap.put("menuLevel", menu.getMenuLevel());
            menuMap.put("sortOrder", menu.getSortOrder());
            menuMap.put("isActive", menu.getIsActive());
            menuMap.put("isNewWindow", menu.getIsNewWindow());
            menuMap.put("menuPath", generateMenuPath(menu));

            if (menu.hasChildren()) {
                menuMap.put("children", convertToJson(menu.getChildren()));
            }

            result.add(menuMap);
        }

        return result;
    }
}
