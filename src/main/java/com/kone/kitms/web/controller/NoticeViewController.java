package com.kone.kitms.web.controller;

import com.kone.kitms.domain.KitmsNotice;
import com.kone.kitms.service.KitmsNoticeService;
import com.kone.kitms.service.dto.CustomReturnDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * KITMS 공지사항 페이지 렌더링 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 공지사항 페이지 렌더링을 담당하는 컨트롤러입니다:
 * - 공지사항 목록 페이지 렌더링 (sub0302.html)
 * - 공지사항 상세 페이지 렌더링 (sub0303.html)
 * - 공지사항 데이터 조회 및 모델에 추가
 * - 에러 처리 및 사용자 피드백
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Controller
public class NoticeViewController {

    private static final Logger log = LoggerFactory.getLogger(NoticeViewController.class);

    private final KitmsNoticeService kitmsNoticeService;

    public NoticeViewController(KitmsNoticeService kitmsNoticeService) {
        this.kitmsNoticeService = kitmsNoticeService;
    }

    /**
     * 공지사항 목록 페이지
     */
    @GetMapping("/sub0302.html")
    public String noticeList() {
        return "sub0302";
    }

    /**
     * 공지사항 상세 페이지
     */
    @GetMapping("/sub0303.html")
    public String noticeDetail(@PathVariable(required = false) Long noticeNo, Model model) {
        log.info("공지사항 상세 페이지 요청: noticeNo={}", noticeNo);
        
        // noticeNo가 URL 파라미터로 전달되지 않으면 쿼리 파라미터에서 가져옴
        if (noticeNo == null) {
            // JavaScript에서 처리하도록 빈 모델로 반환
            return "sub0303";
        }
        
        try {
            // 공지사항 정보 조회
            CustomReturnDTO result = kitmsNoticeService.searchKitmsNotice(noticeNo);
            
            if (result.getStatus() == HttpStatus.OK && result.getData() != null) {
                // 공지사항 정보를 모델에 추가
                Object noticeData = result.getData();
                if (noticeData instanceof KitmsNotice) {
                    model.addAttribute("notice", noticeData);
                } else {
                    // 다른 형태의 데이터 구조 처리
                    model.addAttribute("notice", noticeData);
                }
                log.info("공지사항 정보 로드 완료: {}", noticeNo);
            } else {
                log.warn("공지사항을 찾을 수 없습니다: {}", noticeNo);
                model.addAttribute("error", "공지사항을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("공지사항 조회 오류", e);
            model.addAttribute("error", "공지사항을 불러오는 중 오류가 발생했습니다.");
        }
        
        return "sub0303";
    }
}
