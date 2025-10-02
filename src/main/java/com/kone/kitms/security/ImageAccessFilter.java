package com.kone.kitms.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 이미지 파일 직접 접근 차단 필터
 * 
 * 이 필터는 이미지 파일의 직접 접근을 차단하여 보안을 강화합니다:
 * - /images/** 경로 접근 차단
 * - 이미지 확장자 파일 직접 접근 차단
 * - 보안 이미지 API(/api/secure-images/**)만 허용
 * - 브라우저 개발자 도구 Sources 탭에서 이미지 숨김
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Component
@Order(1) // 높은 우선순위로 설정
public class ImageAccessFilter implements Filter {

    // 차단할 이미지 확장자들
    private static final String[] BLOCKED_EXTENSIONS = {
        ".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico", ".webp", ".bmp", ".tiff"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        
        // 이미지 직접 접근 차단
        if (isImageDirectAccess(requestURI)) {
            // 404 Not Found 응답으로 이미지가 존재하지 않는 것처럼 처리
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            httpResponse.setContentType("text/plain");
            httpResponse.getWriter().write("Resource not found");
            return;
        }
        
        // 보안 이미지 API는 허용
        if (requestURI.startsWith("/api/secure-images/")) {
            chain.doFilter(request, response);
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    /**
     * 이미지 직접 접근 여부 확인
     */
    private boolean isImageDirectAccess(String requestURI) {
        // 보안 이미지 API는 허용
        if (requestURI.startsWith("/api/secure-images/")) {
            return false;
        }
        
        // /images/ 경로 접근 차단
        if (requestURI.startsWith("/images/")) {
            return true;
        }
        
        // 이미지 확장자 파일 직접 접근 차단
        for (String extension : BLOCKED_EXTENSIONS) {
            if (requestURI.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        
        return false;
    }
}
