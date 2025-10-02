package com.kone.kitms.web.rest;

import com.kone.kitms.aop.logging.ExTokenCheck;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * KITMS CSRF 토큰 관리 컨트롤러
 *
 * 이 클래스는 KITMS 시스템의 CSRF 보호를 위한 토큰 관리 기능을 제공합니다:
 * - CSRF 토큰 발급
 * - 토큰 정보 조회
 * - 웹 폼 보안 강화
 *
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api")
public class CsrfController {

    /**
     * CSRF 토큰 정보 조회
     *
     * 웹 폼에서 CSRF 보호를 위해 필요한 토큰 정보를 반환합니다.
     * JavaScript에서 자동으로 CSRF 토큰을 헤더에 추가할 때 사용됩니다.
     *
     * @param request HTTP 요청 객체
     * @return CSRF 토큰 정보 (토큰값, 헤더명, 파라미터명)
     */
    @ExTokenCheck
    @GetMapping("/csrf-token")
    public Map<String, String> csrfToken(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        
        Map<String, String> result = new HashMap<>();
        if (token != null) {
            result.put("token", token.getToken());
            result.put("headerName", token.getHeaderName());
            result.put("parameterName", token.getParameterName());
        } else {
            // 토큰이 없는 경우 빈 값 반환
            result.put("token", "");
            result.put("headerName", "X-XSRF-TOKEN");
            result.put("parameterName", "_csrf");
        }
        
        return result;
    }
}
