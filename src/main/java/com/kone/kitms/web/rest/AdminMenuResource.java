package com.kone.kitms.web.rest;

import com.kone.kitms.domain.AdminMenu;
import com.kone.kitms.domain.KitmsUser;
import com.kone.kitms.repository.KitmsUserRepository;
import com.kone.kitms.service.AdminMenuService;
import com.kone.kitms.service.AdminMenuPermissionService;
import com.kone.kitms.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * KITMS 관리자 메뉴 관리 REST API 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 관리자 메뉴 관리를 위한 모든 기능을 제공합니다:
 * - 관리자 메뉴 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 활성화된 메뉴 조회
 * - 사용자 권한 기반 접근 가능한 메뉴 조회
 * - 메뉴 검색 및 URL 기반 조회
 * - 메뉴 순서 변경 및 상태 토글
 * - 메뉴 통계 조회
 * - 현재 사용자 정보 조회
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api")
public class AdminMenuResource {

    private final Logger log = LoggerFactory.getLogger(AdminMenuResource.class);

    private static final String ENTITY_NAME = "adminMenu";

    private final AdminMenuService adminMenuService;
    private final AdminMenuPermissionService adminMenuPermissionService;
    private final KitmsUserRepository kitmsUserRepository;

    public AdminMenuResource(AdminMenuService adminMenuService, AdminMenuPermissionService adminMenuPermissionService, KitmsUserRepository kitmsUserRepository) {
        this.adminMenuService = adminMenuService;
        this.adminMenuPermissionService = adminMenuPermissionService;
        this.kitmsUserRepository = kitmsUserRepository;
    }

    /**
     * 메뉴 생성
     */
    @PostMapping("/admin-menus")
    public ResponseEntity<AdminMenu> createAdminMenu(@Valid @RequestBody AdminMenu adminMenu) throws URISyntaxException {
        log.debug("REST request to save AdminMenu : {}", adminMenu);
        
        if (adminMenu.getMenuNo() != null) {
            throw new BadRequestAlertException("A new menu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        AdminMenu result = adminMenuService.save(adminMenu);
        
        return ResponseEntity.created(new URI("/api/admin-menus/" + result.getMenuNo()))
                .body(result);
    }

    /**
     * 메뉴 수정
     */
    @PutMapping("/admin-menus/{id}")
    public ResponseEntity<AdminMenu> updateAdminMenu(@PathVariable Long id, @Valid @RequestBody AdminMenu adminMenu) {
        log.debug("REST request to update AdminMenu : {}", adminMenu);
        
        if (adminMenu.getMenuNo() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        if (!id.equals(adminMenu.getMenuNo())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!adminMenuService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        AdminMenu result = adminMenuService.save(adminMenu);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 모든 메뉴 조회
     */
    @GetMapping("/admin-menus")
    public ResponseEntity<List<AdminMenu>> getAllAdminMenus() {
        log.debug("REST request to get all AdminMenus");
        
        List<AdminMenu> menus = adminMenuService.findAll();
        
        return ResponseEntity.ok(menus);
    }

    /**
     * 활성화된 메뉴들을 순서대로 조회 (사이드바용)
     */
    @GetMapping("/admin-menus/active")
    public ResponseEntity<List<AdminMenu>> getActiveMenus() {
        log.debug("REST request to get active AdminMenus");
        
        List<AdminMenu> menus = adminMenuService.findActiveMenus();
        
        return ResponseEntity.ok(menus);
    }

    /**
     * 현재 사용자의 접근 가능한 메뉴들을 순서대로 조회 (권한 기반)
     */
    @GetMapping("/admin-menus/accessible")
    public ResponseEntity<List<AdminMenu>> getAccessibleMenusForCurrentUser() {
        log.debug("REST request to get accessible AdminMenus for current user");
        
        try {
            // 현재 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserId = authentication.getName();
            
            log.debug("Current user ID: {}", currentUserId);
            
            // 현재 사용자의 접근 가능한 메뉴 조회
            List<AdminMenu> accessibleMenus = adminMenuPermissionService.getAccessibleMenus(currentUserId);
            
            log.debug("Accessible menus count: {}", accessibleMenus.size());
            
            return ResponseEntity.ok(accessibleMenus);
        } catch (Exception e) {
            log.error("Error getting accessible menus for current user", e);
            // 오류 발생 시 빈 목록 반환
            return ResponseEntity.ok(List.of());
        }
    }

    /**
     * 메뉴 ID로 조회
     */
    @GetMapping("/admin-menus/{id}")
    public ResponseEntity<AdminMenu> getAdminMenu(@PathVariable Long id) {
        log.debug("REST request to get AdminMenu : {}", id);
        
        Optional<AdminMenu> adminMenu = adminMenuService.findOne(id);
        
        if (adminMenu.isPresent()) {
            return ResponseEntity.ok(adminMenu.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 메뉴 삭제
     */
    @DeleteMapping("/admin-menus/{id}")
    public ResponseEntity<Void> deleteAdminMenu(@PathVariable Long id) {
        log.debug("REST request to delete AdminMenu : {}", id);
        
        try {
            adminMenuService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "deleteerror");
        }
    }


    /**
     * 메뉴명으로 검색
     */
    @GetMapping("/admin-menus/search")
    public ResponseEntity<List<AdminMenu>> searchMenus(@RequestParam String menuName) {
        log.debug("REST request to search AdminMenus by name: {}", menuName);
        
        List<AdminMenu> menus = adminMenuService.searchByMenuName(menuName);
        
        return ResponseEntity.ok(menus);
    }

    /**
     * URL로 메뉴 조회
     */
    @GetMapping("/admin-menus/by-url")
    public ResponseEntity<AdminMenu> getMenuByUrl(@RequestParam String menuUrl) {
        log.debug("REST request to get AdminMenu by URL: {}", menuUrl);
        
        Optional<AdminMenu> menu = adminMenuService.findByMenuUrl(menuUrl);
        
        if (menu.isPresent()) {
            return ResponseEntity.ok(menu.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 메뉴 순서 변경
     */
    @PutMapping("/admin-menus/reorder")
    public ResponseEntity<Void> reorderMenus(@RequestBody List<Long> menuIds) {
        log.debug("REST request to reorder AdminMenus: {}", menuIds);
        
        adminMenuService.updateMenuOrder(menuIds);
        
        return ResponseEntity.ok().build();
    }

    /**
     * 메뉴 활성화/비활성화 토글
     */
    @PutMapping("/admin-menus/{id}/toggle-status")
    public ResponseEntity<Void> toggleMenuStatus(@PathVariable Long id) {
        log.debug("REST request to toggle AdminMenu status: {}", id);
        
        adminMenuService.toggleMenuStatus(id);
        
        return ResponseEntity.ok().build();
    }

    /**
     * 메뉴 통계 조회
     */
    @GetMapping("/admin-menus/stats")
    public ResponseEntity<Map<String, Object>> getMenuStats() {
        log.debug("REST request to get AdminMenu statistics");
        
        Map<String, Object> stats = adminMenuService.getMenuStats();
        
        return ResponseEntity.ok(stats);
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/account")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        log.debug("REST request to get current user");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        
        Optional<KitmsUser> userOpt = kitmsUserRepository.findKitmsUserByUserId(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        KitmsUser user = userOpt.get();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", user.getUserId());
        userInfo.put("userName", user.getUserName());
        userInfo.put("authCode", user.getAuthCode());
        userInfo.put("userEmail", user.getUserEmail());
        userInfo.put("userTel", user.getUserTel());
        userInfo.put("userMobile", user.getUserMobile());
        userInfo.put("enable", user.getEnable());
        
        return ResponseEntity.ok(userInfo);
    }
}
