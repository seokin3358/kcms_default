package com.kone.kitms.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kone.kitms.aop.logging.ExTokenCheck;
import com.kone.kitms.domain.KitmsUser;
import com.kone.kitms.service.KitmsLoginService;
import com.kone.kitms.service.dto.CustomReturnDTO;
import com.kone.kitms.web.rest.vm.KitmsLoginVM;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * KITMS 사용자 인증 및 로그인 관리 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 사용자 인증과 관련된 모든 기능을 제공합니다:
 * - 사용자 로그인 및 토큰 생성
 * - 토큰 갱신
 * - 사용자 ID 찾기
 * - 비밀번호 재설정
 * - 로그아웃 처리
 * - 토큰 유효성 검증
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/kitms-login")
public class KitmsLoginController {

    private final Logger log = LoggerFactory.getLogger(KitmsLoginController.class);

    private final KitmsLoginService kitmsLoginService;

    public KitmsLoginController(KitmsLoginService kitmsLoginService) {
        this.kitmsLoginService = kitmsLoginService;
    }

    /**
     * 사용자 로그인 및 인증 토큰 생성
     * 
     * 사용자의 로그인 정보를 검증하고 JWT 토큰을 생성하여 반환합니다.
     * 
     * @param request HTTP 요청 객체
     * @param loginVM 로그인 정보 (사용자 ID, 비밀번호)
     * @return 인증 결과 및 토큰 정보
     */
    @ExTokenCheck
    @PostMapping("")
    public CustomReturnDTO authorize(HttpServletRequest request, @RequestBody KitmsLoginVM loginVM) {
        CustomReturnDTO result = kitmsLoginService.checkAuthAndCreateToken(request, loginVM);
        
        // JWT 토큰이 있으면 Authorization 헤더로 응답
        if (result.getStatusCode() == 200 && result.getData().containsKey("access_token")) {
            String accessToken = result.getData().get("access_token").toString();
            // 클라이언트가 Authorization 헤더를 사용할 수 있도록 안내
            result.addColumn("authorization_header", "Bearer " + accessToken);
        }
        
        return result;
    }

    /**
     * 토큰 갱신
     * 
     * 기존 토큰을 검증하고 새로운 토큰을 생성하여 반환합니다.
     * 
     * @param request HTTP 요청 객체
     * @return 갱신된 토큰 정보
     */
    @ExTokenCheck
    @GetMapping("/refresh")
    public CustomReturnDTO createRefreshToken(HttpServletRequest request) {
        return kitmsLoginService.createRefreshToken(request);
    }

    /**
     * 사용자 ID 찾기
     * 
     * 사용자의 이메일 또는 전화번호로 사용자 ID를 찾아 반환합니다.
     * 
     * @param kitmsUser 사용자 정보 (이메일 또는 전화번호 포함)
     * @return 사용자 ID 정보
     */
    @ExTokenCheck
    @GetMapping("/id")
    public CustomReturnDTO findUserId(KitmsUser kitmsUser) {
        return kitmsLoginService.findUserIdorResetPassword(kitmsUser, true);
    }

    /**
     * 비밀번호 재설정
     * 
     * 사용자의 이메일 또는 전화번호로 비밀번호 재설정을 처리합니다.
     * 
     * @param kitmsUser 사용자 정보 (이메일 또는 전화번호 포함)
     * @return 비밀번호 재설정 결과
     */
    @ExTokenCheck
    @GetMapping("/pwd")
    public CustomReturnDTO resetUserPassword(KitmsUser kitmsUser) {
        return kitmsLoginService.findUserIdorResetPassword(kitmsUser, false);
    }

    /**
     * 사용자 로그아웃
     * 
     * 사용자의 세션을 종료하고 로그아웃을 처리합니다.
     * 
     * @param request HTTP 요청 객체
     * @param userId 로그아웃할 사용자 ID
     * @return 로그아웃 처리 결과
     */
    @ExTokenCheck
    @GetMapping("/logout")
    public CustomReturnDTO logout(HttpServletRequest request, @RequestParam(value = "userId") String userId) {
        return kitmsLoginService.logout(request, userId);
    }

    /**
     * 토큰 유효성 검증
     * 
     * 현재 요청의 토큰이 유효한지 검증합니다.
     * 
     * @param request HTTP 요청 객체
     * @return 토큰 유효성 검증 결과
     */
    @ExTokenCheck
    @GetMapping("/valid-token")
    public CustomReturnDTO validToken(HttpServletRequest request) {
        return kitmsLoginService.validToken(request);
    }

    /**
     * JWT 인증 응답을 위한 토큰 객체
     * 
     * JWT 토큰과 관련 메시지를 포함하는 응답 객체입니다.
     */
    static class JWTToken {

        private String idToken;
        private String message;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        JWTToken(String idToken, String message) {
            this.idToken = idToken;
            this.message = message;
        }

        @JsonProperty("kitms_token")
        String getIdToken() {
            return idToken;
        }

        @JsonProperty("message")
        String getMessage() {
            return message;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        void setMessage(String message) {
            this.message = message;
        }
    }
}
