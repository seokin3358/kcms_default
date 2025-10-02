
package com.kone.kitms.web.rest;

import com.kone.kitms.domain.KitmsUser;
import com.kone.kitms.repository.KitmsUserRepository;
import com.kone.kitms.service.AdminMenuPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * KITMS 관리자 페이지 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 관리자 페이지 접근을 제어하는 컨트롤러입니다.
 * 사용자 권한에 따른 페이지 접근 제어와 관리자 기능을 제공합니다.
 * 
 * 주요 기능:
 * - 관리자 로그인 페이지 제공
 * - 관리자 대시보드 페이지 제공
 * - 사용자 권한 기반 페이지 접근 제어
 * - 각 관리 기능별 페이지 라우팅
 * - 권한 없는 접근 시 리다이렉트 처리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Controller
public class AdminController {
    
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    
    private final AdminMenuPermissionService adminMenuPermissionService;
    private final KitmsUserRepository kitmsUserRepository;
    
    public AdminController(AdminMenuPermissionService adminMenuPermissionService, KitmsUserRepository kitmsUserRepository) {
        this.adminMenuPermissionService = adminMenuPermissionService;
        this.kitmsUserRepository = kitmsUserRepository;
    }
    
    /**
     * 사용자 권한 체크
     */
    private boolean checkUserPermission(String menuName) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("인증되지 않은 사용자 접근 시도");
                return false;
            }
            
            String userId = authentication.getName();
            Optional<KitmsUser> userOpt = kitmsUserRepository.findKitmsUserByUserId(userId);
            
            if (userOpt.isEmpty()) {
                log.warn("사용자 정보를 찾을 수 없음: {}", userId);
                return false;
            }
            
            KitmsUser user = userOpt.get();
            
            // 메인 관리자(001)는 모든 페이지 접근 가능
            if ("001".equals(user.getAuthCode())) {
                log.info("메인 관리자 접근 허용: {}", userId);
                return true;
            }
            
            // 서브 관리자(002)는 권한 테이블에서 확인
            if ("002".equals(user.getAuthCode())) {
                boolean hasPermission = adminMenuPermissionService.hasMenuPermissionByName(userId, menuName);
                log.info("서브 관리자 권한 체크 결과: {} - {} = {}", userId, menuName, hasPermission);
                return hasPermission;
            }
            
            log.warn("알 수 없는 권한 코드: {} for user: {}", user.getAuthCode(), userId);
            return false;
            
        } catch (Exception e) {
            log.error("권한 체크 중 오류 발생", e);
            return false;
        }
    }
    
    /**
     * 어드민 로그인 페이지
     */
    @GetMapping("/admin/login")
    public String adminLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        log.info("=== Admin Login Page ===");
        
        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        
        if (logout != null) {
            model.addAttribute("message", "로그아웃되었습니다.");
        }
        
        return "forward:/admin/login.html";
    }
    
    /**
     * 어드민 대시보드 페이지
     */
    @GetMapping("/admin/dashboard")
    public String adminDashboard(@RequestParam(value = "error", required = false) String error, Model model) {
        log.info("=== Admin Dashboard Page ===");
        
        // 권한 거부 오류 메시지 처리
        if ("access_denied".equals(error)) {
            model.addAttribute("error", "해당 페이지에 접근할 권한이 없습니다.");
        }
        
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            log.info("인증된 사용자: {}", username);
        }
        
        return "forward:/admin/dashboard.html";
    }
    
    /**
     * 어드민 관리 페이지
     */
    @GetMapping("/admin/management")
    public String adminManagement(Model model) {
        log.info("=== Admin Management Page ===");
        
        // 권한 체크
        if (!checkUserPermission("관리자 관리")) {
            log.warn("관리자 관리 페이지 접근 권한 없음");
            return "redirect:/admin/dashboard?error=access_denied";
        }
        
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            log.info("인증된 사용자: {}", username);
        }
        
        return "forward:/admin/admin-management.html";
    }
    
    /**
     * 컨텐츠 관리 페이지 (CMS 편집기)
     */
    @GetMapping("/admin/content")
    public String contentManagement(Model model) {
        log.info("=== Content Management Page (CMS Editor) ===");
        
        // 권한 체크
        if (!checkUserPermission("콘텐츠 관리")) {
            log.warn("콘텐츠 관리 페이지 접근 권한 없음");
            return "redirect:/admin/dashboard?error=access_denied";
        }
        
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            log.info("인증된 사용자: {}", username);
        }
        
        return "forward:/admin/cms-test.html";
    }
    
    /**
     * 메뉴 관리 페이지
     */
    @GetMapping("/admin/menu")
    public String menuManagement(Model model) {
        log.info("=== Menu Management Page ===");
        
        // 권한 체크
        if (!checkUserPermission("메뉴 관리")) {
            log.warn("메뉴 관리 페이지 접근 권한 없음");
            return "redirect:/admin/dashboard?error=access_denied";
        }
        
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            log.info("인증된 사용자: {}", username);
        }
        
        return "forward:/admin/menu-management.html";
    }
    
    /**
     * 공지사항 관리 페이지
     */
    @GetMapping("/admin/notice")
    public String noticeManagement(Model model) {
        log.info("=== Notice Management Page ===");
        
        // 권한 체크
        if (!checkUserPermission("공지사항 관리")) {
            log.warn("공지사항 관리 페이지 접근 권한 없음");
            return "redirect:/admin/dashboard?error=access_denied";
        }
        
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            log.info("인증된 사용자: {}", username);
        }
        
        return "forward:/admin/notice-management.html";
    }
    
    /**
     * 공지사항 작성/수정 페이지
     */
    @GetMapping("/admin/notice/detail")
    public String noticeDetail(Model model) {
        log.info("=== Notice Detail Page ===");
        
        // 권한 체크
        if (!checkUserPermission("공지사항 관리")) {
            log.warn("공지사항 작성/수정 페이지 접근 권한 없음");
            return "redirect:/admin/dashboard?error=access_denied";
        }
        
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            log.info("인증된 사용자: {}", username);
        }
        
        return "forward:/admin/notice-detail.html";
    }
    
    /**
     * 보도자료 관리 페이지
     */
    @GetMapping("/admin/newsroom")
    public String newsroomManagement(Model model) {
        log.info("=== Newsroom Management Page ===");
        
        // 권한 체크
        if (!checkUserPermission("보도자료 관리")) {
            log.warn("보도자료 관리 페이지 접근 권한 없음");
            return "redirect:/admin/dashboard?error=access_denied";
        }
        
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            log.info("인증된 사용자: {}", username);
        }
        
        return "forward:/admin/newsroom-management.html";
    }
}
