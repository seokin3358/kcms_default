package com.kone.kitms.service;

import com.kone.kitms.domain.AdminMenu;
import com.kone.kitms.domain.AdminMenuPermission;
import com.kone.kitms.domain.KitmsUser;
import com.kone.kitms.repository.AdminMenuPermissionRepository;
import com.kone.kitms.repository.AdminMenuRepository;
import com.kone.kitms.repository.KitmsUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * KITMS 관리자 메뉴 권한 관리 서비스 클래스
 * 
 * 이 클래스는 KITMS 시스템의 관리자 메뉴 권한 관리를 위한 비즈니스 로직을 제공합니다:
 * - 사용자별 메뉴 권한 조회 및 설정
 * - 접근 가능한 메뉴 목록 조회
 * - 메뉴 권한 확인 및 체크
 * - 메뉴별 접근 가능한 사용자 목록 조회
 * - 사용자 권한 삭제 및 관리
 * - 메인 관리자와 서브 관리자 권한 구분
 * - 권한 기반 메뉴 접근 제어
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Service
@Transactional
public class AdminMenuPermissionService {

    private final Logger log = LoggerFactory.getLogger(AdminMenuPermissionService.class);

    private final AdminMenuPermissionRepository adminMenuPermissionRepository;
    private final AdminMenuRepository adminMenuRepository;
    private final KitmsUserRepository kitmsUserRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public AdminMenuPermissionService(AdminMenuPermissionRepository adminMenuPermissionRepository,
                                    AdminMenuRepository adminMenuRepository,
                                    KitmsUserRepository kitmsUserRepository) {
        this.adminMenuPermissionRepository = adminMenuPermissionRepository;
        this.adminMenuRepository = adminMenuRepository;
        this.kitmsUserRepository = kitmsUserRepository;
    }

    /**
     * 사용자의 메뉴 권한 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdminMenuPermission> getUserMenuPermissions(String userId) {
        log.debug("Request to get menu permissions for user: {}", userId);
        try {
            List<AdminMenuPermission> permissions = adminMenuPermissionRepository.findByUserId(userId);
            log.debug("Found {} permissions for user: {}", permissions.size(), userId);
            return permissions;
        } catch (Exception e) {
            log.error("Error getting menu permissions for user: {}", userId, e);
            return List.of(); // 빈 리스트 반환
        }
    }

    /**
     * 사용자가 접근 가능한 메뉴 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdminMenu> getAccessibleMenus(String userId) {
        log.debug("Request to get accessible menus for user: {}", userId);
        
        // 사용자 정보 조회
        Optional<KitmsUser> userOpt = kitmsUserRepository.findKitmsUserByUserId(userId);
        if (userOpt.isEmpty()) {
            log.warn("User not found: {}", userId);
            return List.of();
        }
        
        KitmsUser user = userOpt.get();
        
        // 메인 관리자(001)인 경우 모든 활성 메뉴 반환
        if ("001".equals(user.getAuthCode())) {
            log.debug("User {} is main admin, returning all active menus", userId);
            return adminMenuRepository.findByIsActiveTrueAndIsVisibleTrueOrderByMenuOrder();
        }
        
        // 서브 관리자(002)인 경우 권한이 있는 메뉴만 반환
        List<Long> accessibleMenuNos = adminMenuPermissionRepository.findMenuNosByUserIdAndIsGrantedTrue(userId);
        
        if (accessibleMenuNos.isEmpty()) {
            log.debug("No menu permissions found for user: {}", userId);
            return List.of();
        }
        
        return adminMenuRepository.findAllById(accessibleMenuNos)
                .stream()
                .filter(menu -> menu.getIsActive() && menu.getIsVisible())
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 메뉴 권한 설정
     */
    @Transactional
    public void setUserMenuPermissions(String userId, Map<Long, Boolean> menuPermissions, String updatedBy) {
        log.debug("Request to set menu permissions for user: {}", userId);
        
        // 사용자 정보 조회
        Optional<KitmsUser> userOpt = kitmsUserRepository.findKitmsUserByUserId(userId);
        if (userOpt.isEmpty()) {
            log.warn("User not found: {}", userId);
            throw new RuntimeException("User not found: " + userId);
        }
        
        KitmsUser user = userOpt.get();
        
        // 메인 관리자(001)인 경우 권한 설정 건너뛰기
        if ("001".equals(user.getAuthCode())) {
            log.debug("User {} is main admin, skipping menu permission setting", userId);
            return;
        }
        
        // 기존 권한 삭제 (Native Query로 확실한 삭제)
        log.debug("Deleting existing permissions for user: {}", userId);
        
        // Native Query로 직접 삭제
        int deletedCount = entityManager.createNativeQuery(
            "DELETE FROM admin_menu_permission WHERE user_id = ?")
            .setParameter(1, userId)
            .executeUpdate();
        
        log.debug("Deleted {} existing permissions for user: {}", deletedCount, userId);
        
        // 삭제 후 즉시 flush하여 DB에 반영
        entityManager.flush();
        
        // 새 권한 설정 (권한이 있는 메뉴만 저장)
        for (Map.Entry<Long, Boolean> entry : menuPermissions.entrySet()) {
            Long menuNo = entry.getKey();
            Boolean isGranted = entry.getValue();
            
            // 권한이 있는 메뉴만 저장
            if (isGranted) {
                AdminMenuPermission permission = new AdminMenuPermission();
                permission.setUserId(userId);
                permission.setMenuNo(menuNo);
                permission.setIsGranted(true);
                permission.setCreatedBy(updatedBy);
                permission.setCreatedAt(ZonedDateTime.now());
                permission.setUpdatedBy(updatedBy);
                permission.setUpdatedAt(ZonedDateTime.now());
                
                adminMenuPermissionRepository.save(permission);
                log.debug("Saved permission for user: {} menu: {}", userId, menuNo);
            }
        }
        
        log.debug("Menu permissions set successfully for user: {}", userId);
    }

    /**
     * 사용자의 메뉴 권한 확인
     */
    @Transactional(readOnly = true)
    public boolean hasMenuPermission(String userId, Long menuNo) {
        log.debug("Request to check menu permission for user: {} and menu: {}", userId, menuNo);
        
        // 사용자 정보 조회
        Optional<KitmsUser> userOpt = kitmsUserRepository.findKitmsUserByUserId(userId);
        if (userOpt.isEmpty()) {
            log.warn("User not found: {}", userId);
            return false;
        }
        
        KitmsUser user = userOpt.get();
        
        // 메인 관리자(001)인 경우 모든 메뉴 접근 허용
        if ("001".equals(user.getAuthCode())) {
            log.debug("User {} is main admin, granting access to menu: {}", userId, menuNo);
            return true;
        }
        
        // 서브 관리자(002)인 경우 권한 테이블에서 확인
        return adminMenuPermissionRepository.findByUserIdAndMenuNo(userId, menuNo)
                .map(AdminMenuPermission::getIsGranted)
                .orElse(false);
    }

    /**
     * 메뉴에 접근 가능한 사용자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<String> getUsersWithMenuAccess(Long menuNo) {
        log.debug("Request to get users with access to menu: {}", menuNo);
        return adminMenuPermissionRepository.findUserIdsByMenuNoAndIsGrantedTrue(menuNo);
    }

    /**
     * 사용자 권한 삭제
     */
    public void deleteUserPermissions(String userId) {
        log.debug("Request to delete permissions for user: {}", userId);
        adminMenuPermissionRepository.deleteByUserId(userId);
    }

    /**
     * 메뉴 권한 삭제
     */
    public void deleteMenuPermissions(Long menuNo) {
        log.debug("Request to delete permissions for menu: {}", menuNo);
        adminMenuPermissionRepository.deleteAll(
            adminMenuPermissionRepository.findByUserIdAndIsGrantedTrue("")
                .stream()
                .filter(permission -> permission.getMenuNo().equals(menuNo))
                .collect(Collectors.toList())
        );
    }

    /**
     * 모든 활성 메뉴 목록 조회 (권한 설정용)
     */
    @Transactional(readOnly = true)
    public List<AdminMenu> getAllActiveMenus() {
        log.debug("Request to get all active menus");
        try {
            List<AdminMenu> menus = adminMenuRepository.findByIsActiveTrueAndIsVisibleTrueOrderByMenuOrder();
            log.debug("Found {} active menus", menus.size());
            return menus;
        } catch (Exception e) {
            log.error("Error getting all active menus", e);
            return List.of(); // 빈 리스트 반환
        }
    }

    /**
     * 메뉴명으로 권한 체크
     */
    @Transactional(readOnly = true)
    public boolean hasMenuPermissionByName(String userId, String menuName) {
        log.debug("Request to check menu permission by name for user: {} and menu: {}", userId, menuName);
        
        // 사용자 정보 조회
        Optional<KitmsUser> userOpt = kitmsUserRepository.findKitmsUserByUserId(userId);
        if (userOpt.isEmpty()) {
            log.warn("User not found: {}", userId);
            return false;
        }
        
        KitmsUser user = userOpt.get();
        
        // 메인 관리자(001)인 경우 모든 메뉴 접근 허용
        if ("001".equals(user.getAuthCode())) {
            log.debug("User {} is main admin, granting access to menu: {}", userId, menuName);
            return true;
        }
        
        // 메뉴명으로 메뉴 번호 찾기
        Optional<AdminMenu> menuOpt = adminMenuRepository.findByMenuNameAndIsActiveTrue(menuName);
        if (menuOpt.isEmpty()) {
            log.warn("Menu not found: {}", menuName);
            return false;
        }
        
        Long menuNo = menuOpt.get().getMenuNo();
        
        // 서브 관리자(002)인 경우 권한 테이블에서 확인
        return adminMenuPermissionRepository.findByUserIdAndMenuNo(userId, menuNo)
                .map(AdminMenuPermission::getIsGranted)
                .orElse(false);
    }
}
