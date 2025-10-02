package com.kone.kitms.web.rest;

import com.kone.kitms.domain.KitmsNotice;
import com.kone.kitms.service.KitmsNoticeService;
import com.kone.kitms.service.dto.CustomReturnDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;

/**
 * KITMS 공지사항 관리 REST API 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 공지사항 관리를 위한 REST API를 제공합니다:
 * - 공지사항 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 토큰 없이 관리자용 공지사항 관리
 * - 공지사항 요청 DTO 처리
 * - 에러 처리 및 응답 관리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private static final Logger log = LoggerFactory.getLogger(NoticeController.class);

    private final KitmsNoticeService kitmsNoticeService;

    public NoticeController(KitmsNoticeService kitmsNoticeService) {
        this.kitmsNoticeService = kitmsNoticeService;
    }

    /**
     * 공지사항 작성
     */
    @PostMapping("")
    public ResponseEntity<CustomReturnDTO> createNotice(
            HttpServletRequest request,
            @RequestBody NoticeRequest noticeRequest) {
        
        log.info("공지사항 작성 요청: {}", noticeRequest.getNoticeTitle());
        
        try {
            KitmsNotice notice = new KitmsNotice();
            notice.setNoticeTitle(noticeRequest.getNoticeTitle());
            notice.setNoticeContent(noticeRequest.getNoticeContent());
            notice.setStaticFlag(noticeRequest.getStaticFlag() == 1);
            notice.setCreateDt(ZonedDateTime.now());
            notice.setCreateUserId("admin"); // 관리자 로그인 사용자 ID
            
            CustomReturnDTO result = kitmsNoticeService.createNoticeWithoutToken(notice, null);
            
            if (result.getStatus() == HttpStatus.OK) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            log.error("공지사항 작성 오류", e);
            CustomReturnDTO errorResult = new CustomReturnDTO();
            errorResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResult.setMessage("공지사항 작성 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
    }

    /**
     * 공지사항 수정
     */
    @PutMapping("/{noticeNo}")
    public ResponseEntity<CustomReturnDTO> updateNotice(
            HttpServletRequest request,
            @PathVariable Long noticeNo,
            @RequestBody NoticeRequest noticeRequest) {
        
        log.info("공지사항 수정 요청: noticeNo={}, title={}", noticeNo, noticeRequest.getNoticeTitle());
        
        try {
            KitmsNotice notice = new KitmsNotice();
            notice.setNoticeNo(noticeNo);
            notice.setNoticeTitle(noticeRequest.getNoticeTitle());
            notice.setNoticeContent(noticeRequest.getNoticeContent());
            notice.setStaticFlag(noticeRequest.getStaticFlag() == 1);
            
            CustomReturnDTO result = kitmsNoticeService.updateNoticeWithoutToken(notice, null, null);
            
            if (result.getStatus() == HttpStatus.OK) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            log.error("공지사항 수정 오류", e);
            CustomReturnDTO errorResult = new CustomReturnDTO();
            errorResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResult.setMessage("공지사항 수정 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
    }

    /**
     * 공지사항 조회
     */
    @GetMapping("/{noticeNo}")
    public ResponseEntity<CustomReturnDTO> getNotice(@PathVariable Long noticeNo) {
        log.info("공지사항 조회 요청: noticeNo={}", noticeNo);
        
        try {
            CustomReturnDTO result = kitmsNoticeService.searchKitmsNotice(noticeNo);
            
            if (result.getStatus() == HttpStatus.OK) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("공지사항 조회 오류", e);
            CustomReturnDTO errorResult = new CustomReturnDTO();
            errorResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResult.setMessage("공지사항 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
    }

    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/{noticeNo}")
    public ResponseEntity<CustomReturnDTO> deleteNotice(@PathVariable Long noticeNo) {
        log.info("공지사항 삭제 요청: noticeNo={}", noticeNo);
        
        try {
            CustomReturnDTO result = kitmsNoticeService.deleteNotice(noticeNo);
            
            if (result.getStatus() == HttpStatus.OK) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            log.error("공지사항 삭제 오류", e);
            CustomReturnDTO errorResult = new CustomReturnDTO();
            errorResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResult.setMessage("공지사항 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
    }

    /**
     * 공지사항 요청 DTO
     */
    public static class NoticeRequest {
        private String noticeTitle;
        private String noticeContent;
        private Integer staticFlag;

        // Getters and Setters
        public String getNoticeTitle() {
            return noticeTitle;
        }

        public void setNoticeTitle(String noticeTitle) {
            this.noticeTitle = noticeTitle;
        }

        public String getNoticeContent() {
            return noticeContent;
        }

        public void setNoticeContent(String noticeContent) {
            this.noticeContent = noticeContent;
        }

        public Integer getStaticFlag() {
            return staticFlag;
        }

        public void setStaticFlag(Integer staticFlag) {
            this.staticFlag = staticFlag;
        }
    }
}
