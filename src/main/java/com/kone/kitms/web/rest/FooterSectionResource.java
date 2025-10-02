package com.kone.kitms.web.rest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * KITMS 푸터 섹션 리소스 관리 컨트롤러
 * 
 * 이 클래스는 웹사이트의 푸터 HTML 파일을 제공하는 기능을 담당합니다:
 * - 푸터 HTML 파일 제공
 * - 다양한 경로에서 파일을 찾아 제공하는 기능
 * - UTF-8 인코딩으로 한글 지원
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/footer-section")
public class FooterSectionResource {

    /**
     * 푸터 HTML 파일 제공
     * 
     * 웹사이트의 푸터 HTML 파일을 찾아서 제공합니다.
     * 여러 경로를 순차적으로 확인하여 파일을 찾습니다.
     * 
     * @return 푸터 HTML 내용과 함께 200 상태코드, 또는 파일을 찾을 수 없는 경우 404 상태코드
     */
    @GetMapping("/html")
    public ResponseEntity<String> getFooterSectionHtml() {
        try {
            Resource resource = new ClassPathResource("static/footer.html");
            
            if (!resource.exists()) {
                // webapp 경로에서 찾기
                resource = new ClassPathResource("webapp/footer.html");
            }
            
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            String htmlContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(htmlContent);
                    
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("푸터 섹션을 불러올 수 없습니다: " + e.getMessage());
        }
    }
}
