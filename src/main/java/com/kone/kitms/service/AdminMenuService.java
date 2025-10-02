package com.kone.kitms.service;

import com.kone.kitms.domain.AdminMenu;
import com.kone.kitms.repository.AdminMenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * KITMS 관리자 메뉴 관리 서비스 클래스
 * 
 * 이 클래스는 KITMS 시스템의 관리자 메뉴 관리를 위한 비즈니스 로직을 제공합니다:
 * - 관리자 메뉴 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 활성화된 메뉴 조회 및 정렬
 * - 메뉴 검색 및 URL 기반 조회
 * - 메뉴 순서 변경 및 상태 토글
 * - 메뉴 통계 조회
 * - 생성/수정 일시 및 사용자 추적
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Service
@Transactional
public class AdminMenuService {

    private final Logger log = LoggerFactory.getLogger(AdminMenuService.class);

    private final AdminMenuRepository adminMenuRepository;

    public AdminMenuService(AdminMenuRepository adminMenuRepository) {
        this.adminMenuRepository = adminMenuRepository;
    }

    /**
     * 메뉴 저장
     */
    public AdminMenu save(AdminMenu adminMenu) {
        log.debug("Request to save AdminMenu : {}", adminMenu);
        
        if (adminMenu.getMenuNo() == null) {
            // 새로 생성하는 경우
            adminMenu.setCreatedAt(ZonedDateTime.now());
            adminMenu.setCreatedBy("admin"); // TODO: 실제 사용자 정보로 변경
        } else {
            // 수정하는 경우
            adminMenu.setUpdatedAt(ZonedDateTime.now());
            adminMenu.setUpdatedBy("admin"); // TODO: 실제 사용자 정보로 변경
        }
        
        // 계층구조 제거로 레벨 설정 로직 제거
        
        return adminMenuRepository.save(adminMenu);
    }

    /**
     * 모든 메뉴 조회
     */
    @Transactional(readOnly = true)
    public List<AdminMenu> findAll() {
        log.debug("Request to get all AdminMenus");
        return adminMenuRepository.findAll();
    }

    /**
     * 활성화된 메뉴들을 순서대로 조회
     */
    @Transactional(readOnly = true)
    public List<AdminMenu> findActiveMenus() {
        log.debug("Request to get active AdminMenus");
        
        return adminMenuRepository.findActiveMenusOrderByOrder();
    }

    /**
     * 메뉴 ID로 조회
     */
    @Transactional(readOnly = true)
    public Optional<AdminMenu> findOne(Long id) {
        log.debug("Request to get AdminMenu : {}", id);
        return adminMenuRepository.findById(id);
    }

    /**
     * 메뉴 삭제
     */
    public void delete(Long id) {
        log.debug("Request to delete AdminMenu : {}", id);
        adminMenuRepository.deleteById(id);
    }

    /**
     * 메뉴명으로 검색
     */
    @Transactional(readOnly = true)
    public List<AdminMenu> searchByMenuName(String menuName) {
        log.debug("Request to search AdminMenus by name: {}", menuName);
        return adminMenuRepository.findByMenuNameContaining(menuName);
    }

    /**
     * URL로 메뉴 조회
     */
    @Transactional(readOnly = true)
    public Optional<AdminMenu> findByMenuUrl(String menuUrl) {
        log.debug("Request to get AdminMenu by URL: {}", menuUrl);
        AdminMenu menu = adminMenuRepository.findByMenuUrl(menuUrl);
        return Optional.ofNullable(menu);
    }

    /**
     * 메뉴 순서 변경
     */
    public void updateMenuOrder(List<Long> menuIds) {
        log.debug("Request to update menu order: {}", menuIds);
        
        for (int i = 0; i < menuIds.size(); i++) {
            Optional<AdminMenu> menuOpt = adminMenuRepository.findById(menuIds.get(i));
            if (menuOpt.isPresent()) {
                AdminMenu menu = menuOpt.get();
                menu.setMenuOrder(i + 1);
                menu.setUpdatedAt(ZonedDateTime.now());
                menu.setUpdatedBy("admin");
                adminMenuRepository.save(menu);
            }
        }
    }

    /**
     * 메뉴 활성화/비활성화
     */
    public void toggleMenuStatus(Long menuId) {
        log.debug("Request to toggle menu status: {}", menuId);
        
        Optional<AdminMenu> menuOpt = adminMenuRepository.findById(menuId);
        if (menuOpt.isPresent()) {
            AdminMenu menu = menuOpt.get();
            menu.setIsActive(!menu.getIsActive());
            menu.setUpdatedAt(ZonedDateTime.now());
            menu.setUpdatedBy("admin");
            adminMenuRepository.save(menu);
        }
    }

    /**
     * 활성 메뉴 개수 조회
     */
    @Transactional(readOnly = true)
    public long countActiveMenus() {
        log.debug("Request to count active AdminMenus");
        return adminMenuRepository.countActiveMenus();
    }

    /**
     * 메뉴 통계 조회
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getMenuStats() {
        log.debug("Request to get AdminMenu statistics");
        
        long totalCount = adminMenuRepository.count();
        long activeCount = adminMenuRepository.countActiveMenus();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", totalCount);
        stats.put("activeCount", activeCount);
        
        return stats;
    }
}
