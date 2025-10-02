package com.kone.kitms.web.rest;

import com.kone.kitms.domain.AdminMenu;
import com.kone.kitms.domain.AdminMenuPermission;
import com.kone.kitms.service.AdminMenuPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KITMS 관리자 메뉴 권한 관리 REST API 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 관리자 메뉴 권한 관리를 위한 모든 기능을 제공합니다:
 * - 사용자별 메뉴 권한 조회 및 설정
 * - 접근 가능한 메뉴 목록 조회
 * - 메뉴 권한 확인 및 체크
 * - 메뉴별 접근 가능한 사용자 목록 조회
 * - 사용자 권한 삭제
 * - 모든 활성 메뉴 목록 조회
 * - 페이지 접근 권한 체크
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api")
public class AdminMenuPermissionResource {

    private final Logger log = LoggerFactory.getLogger(AdminMenuPermissionResource.class);

    private final AdminMenuPermissionService adminMenuPermissionService;

    public AdminMenuPermissionResource(AdminMenuPermissionService adminMenuPermissionService) {
        this.adminMenuPermissionService = adminMenuPermissionService;
    }

    /**
     * 사용자의 메뉴 권한 목록 조회
     */
    @GetMapping("/admin-menu-permissions/user/{userId}")
    public ResponseEntity<List<AdminMenuPermission>> getUserMenuPermissions(@PathVariable String userId) {
        log.debug("REST request to get menu permissions for user: {}", userId);
        
        List<AdminMenuPermission> permissions = adminMenuPermissionService.getUserMenuPermissions(userId);
        return ResponseEntity.ok(permissions);
    }

    /**
     * 사용자가 접근 가능한 메뉴 목록 조회
     */
    @GetMapping("/admin-menu-permissions/accessible/{userId}")
    public ResponseEntity<List<AdminMenu>> getAccessibleMenus(@PathVariable String userId) {
        log.debug("REST request to get accessible menus for user: {}", userId);
        
        List<AdminMenu> menus = adminMenuPermissionService.getAccessibleMenus(userId);
        return ResponseEntity.ok(menus);
    }

    /**
     * 사용자의 메뉴 권한 설정
     */
    @PostMapping("/admin-menu-permissions/user/{userId}")
    public ResponseEntity<Map<String, Object>> setUserMenuPermissions(
            @PathVariable String userId,
            @RequestBody Map<Long, Boolean> menuPermissions,
            @RequestParam(defaultValue = "admin") String updatedBy) {
        
        log.debug("REST request to set menu permissions for user: {}", userId);
        
        try {
            adminMenuPermissionService.setUserMenuPermissions(userId, menuPermissions, updatedBy);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "메뉴 권한이 성공적으로 저장되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error setting menu permissions for user: {}", userId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "메뉴 권한 저장 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 사용자의 메뉴 권한 확인
     */
    @GetMapping("/admin-menu-permissions/check/{userId}/{menuNo}")
    public ResponseEntity<Map<String, Object>> checkMenuPermission(
            @PathVariable String userId,
            @PathVariable Long menuNo) {
        
        log.debug("REST request to check menu permission for user: {} and menu: {}", userId, menuNo);
        
        boolean hasPermission = adminMenuPermissionService.hasMenuPermission(userId, menuNo);
        
        Map<String, Object> response = new HashMap<>();
        response.put("hasPermission", hasPermission);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 메뉴에 접근 가능한 사용자 목록 조회
     */
    @GetMapping("/admin-menu-permissions/menu/{menuNo}/users")
    public ResponseEntity<List<String>> getUsersWithMenuAccess(@PathVariable Long menuNo) {
        log.debug("REST request to get users with access to menu: {}", menuNo);
        
        List<String> userIds = adminMenuPermissionService.getUsersWithMenuAccess(menuNo);
        return ResponseEntity.ok(userIds);
    }

    /**
     * 사용자 권한 삭제
     */
    @DeleteMapping("/admin-menu-permissions/user/{userId}")
    public ResponseEntity<Void> deleteUserPermissions(@PathVariable String userId) {
        log.debug("REST request to delete permissions for user: {}", userId);
        
        adminMenuPermissionService.deleteUserPermissions(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 모든 활성 메뉴 목록 조회 (권한 설정용)
     */
    @GetMapping("/admin-menu-permissions/all-menus")
    public ResponseEntity<List<AdminMenu>> getAllActiveMenus() {
        log.debug("REST request to get all active menus");
        
        List<AdminMenu> menus = adminMenuPermissionService.getAllActiveMenus();
        return ResponseEntity.ok(menus);
    }

    /**
     * 사용자의 메뉴 권한 설정 (체크박스 형태)
     */
    @GetMapping("/admin-menu-permissions/user/{userId}/with-menus")
    public ResponseEntity<Map<String, Object>> getUserMenuPermissionsWithMenus(@PathVariable String userId) {
        log.debug("REST request to get menu permissions with menu details for user: {}", userId);
        
        try {
            List<AdminMenu> allMenus = adminMenuPermissionService.getAllActiveMenus();
            List<AdminMenuPermission> userPermissions = adminMenuPermissionService.getUserMenuPermissions(userId);
            
            // 권한을 Map으로 변환 (빈 리스트 처리)
            Map<Long, Boolean> permissionMap = new HashMap<>();
            if (userPermissions != null && !userPermissions.isEmpty()) {
                for (AdminMenuPermission permission : userPermissions) {
                    permissionMap.put(permission.getMenuNo(), permission.getIsGranted());
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("allMenus", allMenus);
            response.put("userPermissions", permissionMap);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting menu permissions with menus for user: {}", userId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "메뉴 권한 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 페이지 접근 권한 체크 (메뉴명 기반)
     */
    @PostMapping("/admin-menu-permissions/check")
    public ResponseEntity<Map<String, Object>> checkPagePermission(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String menuName = request.get("menuName");
        
        log.debug("REST request to check page permission for user: {} and menu: {}", userId, menuName);
        
        boolean hasAccess = adminMenuPermissionService.hasMenuPermissionByName(userId, menuName);
        
        Map<String, Object> response = new HashMap<>();
        response.put("hasAccess", hasAccess);
        response.put("userId", userId);
        response.put("menuName", menuName);
        
        return ResponseEntity.ok(response);
    }
}
