package com.kone.kitms.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * KITMS CMS 템플릿 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 CMS 템플릿 페이지 요청을 처리하는 컨트롤러입니다:
 * - /cms-template.html?page=xxx 형태의 요청 처리
 * - 페이지 코드에 따른 동적 템플릿 선택
 * - 특정 페이지(sub0301, sub0302)의 별도 템플릿 처리
 * - DB 기반 컨텐츠와 정적 템플릿 구분 처리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Controller
public class CmsTemplateController {

    private static final Logger log = LoggerFactory.getLogger(CmsTemplateController.class);

    /**
     * CMS 템플릿 페이지 처리
     * @param page 페이지 코드 (예: sub0302, sub0101 등)
     * @param model Thymeleaf 모델
     * @return 템플릿 이름
     */
    @GetMapping("/cms-template.html")
    public String cmsTemplate(@RequestParam(value = "page", defaultValue = "sub0101") String page, Model model) {
        log.info("CMS 템플릿 요청 - page: {}", page);
        
        // 페이지 코드를 모델에 추가
        model.addAttribute("pageCode", page);
        
        // sub0302 페이지는 별도 템플릿으로 처리 (DB 사용 안함)
        if ("sub0302".equals(page)) {
            return "sub0302";
        }else if("sub0301".equals(page)) {
            return "sub0301";
        }
        
        // 다른 페이지들은 DB에서 컨텐츠를 가져오는 cms-template 템플릿으로 처리
        return "cms-template";
    }
}
