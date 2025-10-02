package com.kone.kitms.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * KITMS SPA 웹 필터 클래스
 * 
 * 이 클래스는 KITMS 시스템의 SPA(Single Page Application) 요청 처리를 위한 필터입니다:
 * - API 요청이 아닌 모든 요청을 main.html로 포워딩
 * - 정적 리소스 요청 제외 (점이 포함된 경로)
 * - API, 관리, 문서, 관리자 경로 제외
 * - SPA 라우팅 지원
 * - 클라이언트 사이드 라우팅 처리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public class SpaWebFilter extends OncePerRequestFilter {

    /**
     * Forwards any unmapped paths (except those containing a period) to the client {@code index.html}.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        // Request URI includes the contextPath if any, removed it.
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (
            !path.startsWith("/api") &&
            !path.startsWith("/management") &&
            !path.startsWith("/v3/api-docs") &&
            !path.startsWith("/admin") &&
            !path.contains(".") &&
            path.matches("/(.*)")
        ) {
            request.getRequestDispatcher("/main.html").forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
