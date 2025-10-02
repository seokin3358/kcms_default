package com.kone.kitms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * KITMS JWT 인증 필터 클래스
 * 
 * 이 클래스는 HTTP 요청에서 JWT 토큰을 추출하고 검증하여
 * Spring Security 컨텍스트에 인증 정보를 설정하는 필터입니다:
 * - Authorization 헤더에서 JWT 토큰 추출
 * - JWT 토큰 유효성 검증
 * - 토큰에서 사용자 정보 추출
 * - Spring Security 인증 컨텍스트 설정
 * - 인증되지 않은 요청은 다음 필터로 전달
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
// JWT 기능을 일시적으로 비활성화 (설정 누락으로 인한 오류 방지)
// @Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * HTTP 요청을 필터링하여 JWT 토큰을 검증하고 인증 정보를 설정
     * 
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException IO 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // JWT 토큰 추출
            String jwt = getJwtFromRequest(request);
            
            // 토큰이 존재하고 유효한 경우
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                
                // 토큰에서 사용자 정보 추출
                Long userId = tokenProvider.getUserIdFromToken(jwt);
                String username = tokenProvider.getUsernameFromToken(jwt);
                String authorities = tokenProvider.getAuthoritiesFromToken(jwt);
                
                log.debug("JWT 토큰 검증 성공 - 사용자 ID: {}, 사용자명: {}", userId, username);
                
                // 권한 정보를 GrantedAuthority 리스트로 변환
                List<SimpleGrantedAuthority> authorityList = Arrays.stream(authorities.split(","))
                        .map(String::trim)
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                
                // UserPrincipal 생성
                UserPrincipal userPrincipal = new UserPrincipal(userId, username, null, null, authorityList);
                
                // Spring Security 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userPrincipal, null, authorityList);
                
                // 요청 세부 정보 설정
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Security Context에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("사용자 인증 정보가 Security Context에 설정되었습니다: {}", username);
                
            } else if (StringUtils.hasText(jwt)) {
                log.warn("유효하지 않은 JWT 토큰입니다: {}", jwt.substring(0, Math.min(jwt.length(), 20)) + "...");
            }
            
        } catch (Exception ex) {
            log.error("Security Context에 사용자 인증 정보를 설정할 수 없습니다", ex);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청에서 JWT 토큰을 추출
     * 
     * @param request HTTP 요청 객체
     * @return JWT 토큰 문자열 (없으면 null)
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        // KITMS 커스텀 헤더도 확인 (기존 시스템 호환성)
        String kitmsToken = request.getHeader("kitms_token");
        if (StringUtils.hasText(kitmsToken)) {
            return kitmsToken;
        }
        
        return null;
    }

    /**
     * 특정 요청 경로는 JWT 필터를 건너뛰도록 설정
     * 
     * @param request HTTP 요청 객체
     * @return 필터를 건너뛸지 여부
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // 정적 리소스는 필터 건너뛰기 (이미지 제외)
        if (path.startsWith("/static/") || 
            // path.startsWith("/images/") || // 제거됨 - 이미지 직접 접근 차단
            path.startsWith("/js/") || 
            path.startsWith("/style/") || 
            path.startsWith("/fonts/") ||
            path.endsWith(".html") ||
            path.endsWith(".js") ||
            path.endsWith(".css") ||
            path.endsWith(".ico") ||
            // path.endsWith(".png") || // 제거됨 - 이미지 직접 접근 차단
            // path.endsWith(".svg") || // 제거됨 - 이미지 직접 접근 차단
            path.endsWith(".json") ||
            path.endsWith(".map") ||
            path.endsWith(".txt") ||
            path.endsWith(".webapp")) {
            return true;
        }
        
        // 로그인 관련 API는 필터 건너뛰기
        if (path.startsWith("/api/kitms-login") && 
            (path.endsWith("/login") || path.endsWith("/id") || path.endsWith("/pwd"))) {
            return true;
        }
        
        return false;
    }
}
