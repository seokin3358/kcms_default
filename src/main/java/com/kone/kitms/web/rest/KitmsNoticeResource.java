package com.kone.kitms.web.rest;

import com.kone.kitms.aop.logging.ExTokenCheck;
import com.kone.kitms.domain.KitmsNotice;
import com.kone.kitms.mybatis.vo.KitmsCommonParamVO;
import com.kone.kitms.service.KitmsNoticeService;
import com.kone.kitms.service.dto.CustomReturnDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * KITMS 공지사항 관리 REST API 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 공지사항 관리를 위한 모든 기능을 제공합니다:
 * - 공지사항 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 공지사항 목록 조회 (전체, 공개, 고정)
 * - 첨부파일 관리
 * - 공지사항 검색 및 페이징
 * - 고정 공지사항 관리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/kitms-notices")
@Transactional
public class KitmsNoticeResource {

    private final Logger log = LoggerFactory.getLogger(KitmsNoticeResource.class);

    private static final String ENTITY_NAME = "kitmsNotice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KitmsNoticeService kitmsNoticeService;

    public KitmsNoticeResource(KitmsNoticeService kitmsNoticeService) {
        this.kitmsNoticeService = kitmsNoticeService;
    }

    /**
     * {@code POST  /kitms-notices} : Create a new kitmsNotice.
     *
     * @param kitmsNotice the kitmsNotice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kitmsNotice, or with status {@code 400 (Bad Request)} if the kitmsNotice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public CustomReturnDTO createKitmsNotice(
        HttpServletRequest request,
        @RequestPart(value = "params") KitmsNotice kitmsNotice,
        @RequestPart(value = "attachList", required = false) List<MultipartFile> multipartFileList
    ) throws URISyntaxException, IOException {
        return kitmsNoticeService.createNotice(request, kitmsNotice, multipartFileList);
    }

    /**
     * {@code POST  /api/notice} : Create a new notice with JSON data.
     */
    @PostMapping("/api/notice")
    public CustomReturnDTO createNoticeJson(
        HttpServletRequest request,
        @RequestBody KitmsNotice kitmsNotice
    ) throws URISyntaxException, IOException {
        return kitmsNoticeService.createNotice(request, kitmsNotice, null);
    }

    /**
     * {@code PUT  /kitms-notices/:noticeNo} : Updates an existing kitmsNotice.
     *
     * @param kitmsNotice the kitmsNotice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kitmsNotice,
     * or with status {@code 400 (Bad Request)} if the kitmsNotice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kitmsNotice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public CustomReturnDTO updateKitmsNotice(
        HttpServletRequest request,
        @RequestPart(value = "params") KitmsNotice kitmsNotice,
        @RequestPart(value = "attachList", required = false) List<MultipartFile> multipartFileList,
        @RequestPart(value = "deleteAttachNoList", required = false) List<Long> deleteAttachNoList
    ) throws URISyntaxException, IOException {
        return kitmsNoticeService.updateNotice(request, kitmsNotice, multipartFileList, deleteAttachNoList);
    }

    /**
     * {@code PUT  /api/notice/{noticeNo}} : Update an existing notice with JSON data.
     */
    @PutMapping("/api/notice/{noticeNo}")
    public CustomReturnDTO updateNoticeJson(
        HttpServletRequest request,
        @PathVariable Long noticeNo,
        @RequestBody KitmsNotice kitmsNotice
    ) throws URISyntaxException, IOException {
        kitmsNotice.setNoticeNo(noticeNo);
        return kitmsNoticeService.updateNotice(request, kitmsNotice, null, null);
    }

    /**
     * {@code GET  /kitms-notices} : get all the kitmsNotices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kitmsNotices in body.
     */
    @ExTokenCheck
    @GetMapping("/all")
    public CustomReturnDTO getAllKitmsNotices(KitmsCommonParamVO kitmsCommonParamVO) {
        return kitmsNoticeService.getNoticeAllList(kitmsCommonParamVO);
    }

    /**
     * {@code GET  /kitms-notices/public} : get public kitmsNotices without authentication.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of public kitmsNotices in body.
     */
    @ExTokenCheck
    @GetMapping(value = "/public", produces = "application/json; charset=UTF-8")
    public CustomReturnDTO getPublicKitmsNotices(KitmsCommonParamVO kitmsCommonParamVO) {
        log.info("공지사항 public API 호출 - page: {}, size: {}, searchColumn: {}, searchValue: {}", 
            kitmsCommonParamVO.getPage(), kitmsCommonParamVO.getSize(), 
            kitmsCommonParamVO.getSearchColumn(), kitmsCommonParamVO.getSearchValue());
        return kitmsNoticeService.getNoticeAllList(kitmsCommonParamVO);
    }

    /**
     * {@code GET  /kitms-notices/static} : get static (pinned) kitmsNotices for main page.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of static kitmsNotices in body.
     */
    @ExTokenCheck
    @GetMapping("/static")
    public CustomReturnDTO getStaticKitmsNotices(KitmsCommonParamVO kitmsCommonParamVO) {
        return kitmsNoticeService.getStaticNoticeList(kitmsCommonParamVO);
    }

    /**
     * {@code GET  /kitms-notices/:id} : get the "id" kitmsNotice.
     *
     * @param id the id of the kitmsNotice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kitmsNotice, or with status {@code 404 (Not Found)}.
     */
    @ExTokenCheck
    @GetMapping("/{noticeNo}")
    public CustomReturnDTO getKitmsNotice(@PathVariable("noticeNo") Long id) {
        return kitmsNoticeService.searchKitmsNotice(id);
    }

    /**
     * {@code DELETE  /kitms-notices/:id} : delete the "id" kitmsNotice.
     *
     * @param id the id of the kitmsNotice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{noticeNo}")
    public CustomReturnDTO deleteKitmsNotice(@PathVariable("noticeNo") Long noticeNo) {
        return kitmsNoticeService.deleteNotice(noticeNo);
    }
}
