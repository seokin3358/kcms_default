package com.kone.kitms.web.rest;

import com.kone.kitms.aop.logging.ExTokenCheck;
import com.kone.kitms.service.KitmsUserService;
import com.kone.kitms.service.dto.CustomReturnDTO;
import com.kone.kitms.service.dto.KitmsUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * KITMS 사용자 관리 REST API 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 사용자 관리를 위한 모든 기능을 제공합니다:
 * - 사용자 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 사용자 목록 조회
 * - 현재 로그인 사용자 정보 조회
 * - 사용자 권한 관리
 * - 사용자 계정 활성화/비활성화
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/kitms-users")
public class KitmsUserResource {

    private final Logger log = LoggerFactory.getLogger(KitmsUserResource.class);

    @Autowired
    private KitmsUserService kitmsUserService;

    /**
     * 모든 사용자 목록 조회
     * GET /api/kitms-users/all
     */
    @ExTokenCheck
    @GetMapping("/all")
    public CustomReturnDTO getAllUsers(HttpServletRequest request) {
        log.info("사용자 목록 조회 요청");
        return kitmsUserService.getAllUsers();
    }

    /**
     * 특정 사용자 조회
     * GET /api/kitms-users/{userId}
     */
    @ExTokenCheck
    @GetMapping("/{userId}")
    public CustomReturnDTO getUserById(@PathVariable String userId, HttpServletRequest request) {
        log.info("사용자 조회 요청: {}", userId);
        return kitmsUserService.getUserById(userId);
    }

    /**
     * 새 사용자 생성
     * POST /api/kitms-users
     */
    @ExTokenCheck
    @PostMapping("")
    public CustomReturnDTO createUser(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) {
        log.info("사용자 생성 요청");
        try {
            // JSON 파라미터 파싱
            log.info("받은 params: {}", paramMap);
            
            log.info("파싱된 paramMap: {}", paramMap);
            
            KitmsUserDTO userDTO = new KitmsUserDTO();
            userDTO.setUserId((String) paramMap.get("userId"));
            
            // userName이 null이거나 빈 문자열인 경우 기본값 설정
            String userName = (String) paramMap.get("userName");
            if (userName == null || userName.trim().isEmpty()) {
                userName = (String) paramMap.get("userId"); // userId를 userName으로 사용
                log.warn("userName이 null이므로 userId를 userName으로 사용: {}", userName);
            }
            userDTO.setUserName(userName);
            
            userDTO.setUserPwd((String) paramMap.get("password"));
            userDTO.setUserEmail((String) paramMap.get("userEmail"));
            userDTO.setUserTel((String) paramMap.get("userPhone"));
            userDTO.setUserMobile((String) paramMap.get("userPhone"));
            userDTO.setAuthCode((String) paramMap.get("userRole"));
            userDTO.setUserRole((String) paramMap.get("userRole"));
            userDTO.setEnable("1".equals(paramMap.get("useFlag")));
            userDTO.setUserEtc("");
            
            log.info("생성된 userDTO: userId={}, userName={}, userEmail={}", 
                    userDTO.getUserId(), userDTO.getUserName(), userDTO.getUserEmail());
            
            return kitmsUserService.createUser(userDTO);
        } catch (Exception e) {
            log.error("사용자 생성 중 오류 발생", e);
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            result.setMessage("사용자 생성 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }

    /**
     * 사용자 정보 수정
     * PUT /api/kitms-users
     */
    @ExTokenCheck
    @PutMapping("")
    public CustomReturnDTO updateUser(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) {
        log.info("사용자 수정 요청");
        try {
            // JSON 파라미터 파싱
            log.info("받은 params (수정): {}", paramMap);
            
            log.info("파싱된 paramMap (수정): {}", paramMap);
            
            KitmsUserDTO userDTO = new KitmsUserDTO();
            userDTO.setUserId((String) paramMap.get("userId"));
            
            // userName이 null이거나 빈 문자열인 경우 기본값 설정
            String userName = (String) paramMap.get("userName");
            if (userName == null || userName.trim().isEmpty()) {
                userName = (String) paramMap.get("userId"); // userId를 userName으로 사용
                log.warn("userName이 null이므로 userId를 userName으로 사용 (수정): {}", userName);
            }
            userDTO.setUserName(userName);
            
            userDTO.setUserPwd((String) paramMap.get("password"));
            userDTO.setUserEmail((String) paramMap.get("userEmail"));
            userDTO.setUserTel((String) paramMap.get("userPhone"));
            userDTO.setUserMobile((String) paramMap.get("userPhone"));
            userDTO.setAuthCode((String) paramMap.get("userRole"));
            userDTO.setUserRole((String) paramMap.get("userRole"));
            userDTO.setEnable("1".equals(paramMap.get("useFlag")));
            userDTO.setUserEtc("");
            
            log.info("생성된 userDTO (수정): userId={}, userName={}, userEmail={}", 
                    userDTO.getUserId(), userDTO.getUserName(), userDTO.getUserEmail());
            
            return kitmsUserService.updateUser(userDTO);
        } catch (Exception e) {
            log.error("사용자 수정 중 오류 발생", e);
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            result.setMessage("사용자 수정 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     * GET /api/kitms-users/current
     */
    @ExTokenCheck
    @GetMapping("/current")
    public CustomReturnDTO getCurrentUser(HttpServletRequest request) {
        log.info("현재 사용자 정보 조회 요청");
        return kitmsUserService.getCurrentUser();
    }

    /**
     * 사용자 삭제
     * DELETE /api/kitms-users/{userId}
     */
    @ExTokenCheck
    @DeleteMapping("/{userId}")
    public CustomReturnDTO deleteUser(@PathVariable String userId, HttpServletRequest request) {
        log.info("사용자 삭제 요청: {}", userId);
        return kitmsUserService.deleteUser(userId);
    }
}
