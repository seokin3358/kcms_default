package com.kone.kitms.service;

// QueryDSL imports removed for now

import com.kone.kitms.domain.KitmsNotice;
import com.kone.kitms.mybatis.mapper.KitmsNoticeMapper;
import com.kone.kitms.mybatis.vo.KitmsCommonParamVO;
import com.kone.kitms.mybatis.vo.KitmsNoticeVO;
import com.kone.kitms.repository.KitmsAttachRepository;
import com.kone.kitms.repository.KitmsNoticeRepository;
import com.kone.kitms.service.dto.CustomReturnDTO;
import com.kone.kitms.service.dto.KitmsNoticeDTO;
import com.kone.kitms.service.dto.PagingListDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * KITMS 공지사항 관리 서비스
 * 
 * 시스템 공지사항의 CRUD 작업 및 공지사항 관련 비즈니스 로직을 처리하는 서비스입니다.
 * 
 * 주요 기능:
 * - 공지사항 목록 조회 및 페이징 처리
 * - 공지사항 생성, 수정, 삭제
 * - 고정 공지사항 관리 (상단 고정)
 * - 공지사항 첨부파일 관리
 * - 공지사항 검색 및 필터링
 * - CKEditor를 통한 리치 텍스트 편집 지원
 * 
 * @author KITMS Development Team
 * @version 1.0
 */
@Service
@Transactional
public class KitmsNoticeService {

    private final Logger log = LoggerFactory.getLogger(KitmsNoticeService.class);

    private final KitmsNoticeRepository kitmsNoticeRepository;

    private final KitmsNoticeMapper kitmsNoticeMapper;

    private static final String TABLE_NAME = "kitms_notice";

    private final FileService fileService;

    private final KitmsAttachRepository kitmsAttachRepository;

    private final KitmsLoginService kitmsLoginService;

    @Value("${notice.static.count:3}")
    private int noticeStaticCount;

    public KitmsNoticeService(
        KitmsNoticeRepository kitmsNoticeRepository,
        KitmsNoticeMapper kitmsNoticeMapper,
        FileService fileService,
        KitmsAttachRepository kitmsAttachRepository,
        KitmsLoginService kitmsLoginService
    ) {
        this.kitmsNoticeRepository = kitmsNoticeRepository;
        this.kitmsNoticeMapper = kitmsNoticeMapper;
        this.fileService = fileService;
        this.kitmsAttachRepository = kitmsAttachRepository;
        this.kitmsLoginService = kitmsLoginService;
    }

    public CustomReturnDTO createNotice(HttpServletRequest request, KitmsNotice kitmsNotice, List<MultipartFile> multipartFileList)
        throws IOException {
        CustomReturnDTO result = new CustomReturnDTO();
        String noticeTitle = kitmsNotice.getNoticeTitle();
        String noticeContent = kitmsNotice.getNoticeContent();

        result.setStatus(HttpStatus.BAD_REQUEST);
        if (noticeTitle == null || noticeTitle.isEmpty() || noticeTitle.isBlank()) {
            result.setMessage("공지사항 제목은 필수값입니다.");
            return result;
        }
        if (noticeContent == null || noticeContent.isEmpty() || noticeContent.isBlank()) {
            result.setMessage("공지사항 내용은 필수값입니다.");
            return result;
        }

        kitmsNotice.setCreateDt(ZonedDateTime.now().plusHours(9));
        kitmsNotice.setCreateUserId(kitmsLoginService.getTokenUserId(request));

        KitmsNotice resultInfo = kitmsNoticeRepository.save(kitmsNotice);
        fileService.createFile(request, "kitms_notice", resultInfo.getNoticeNo(), multipartFileList, false);

        result.setStatus(HttpStatus.OK);
        result.addColumn("resultInfo", resultInfo);

        return result;
    }

    /**
     * JWT 토큰 없이 공지사항 작성 (관리자용)
     */
    public CustomReturnDTO createNoticeWithoutToken(KitmsNotice kitmsNotice, List<MultipartFile> multipartFileList) {
        CustomReturnDTO result = new CustomReturnDTO();
        String noticeTitle = kitmsNotice.getNoticeTitle();
        String noticeContent = kitmsNotice.getNoticeContent();

        result.setStatus(HttpStatus.BAD_REQUEST);
        if (noticeTitle == null || noticeTitle.isEmpty() || noticeTitle.isBlank()) {
            result.setMessage("공지사항 제목은 필수값입니다.");
            return result;
        }
        if (noticeContent == null || noticeContent.isEmpty() || noticeContent.isBlank()) {
            result.setMessage("공지사항 내용은 필수값입니다.");
            return result;
        }

        // createDt와 createUserId는 이미 NoticeController에서 설정됨
        KitmsNotice resultInfo = kitmsNoticeRepository.save(kitmsNotice);
        
        // 파일 업로드는 선택사항이므로 null 체크
        if (multipartFileList != null && !multipartFileList.isEmpty()) {
            try {
                fileService.createFile(null, "kitms_notice", resultInfo.getNoticeNo(), multipartFileList, false);
            } catch (IOException e) {
                log.warn("파일 업로드 중 오류 발생: {}", e.getMessage());
            }
        }

        result.setStatus(HttpStatus.OK);
        result.addColumn("resultInfo", resultInfo);

        return result;
    }

    public CustomReturnDTO updateNotice(
        HttpServletRequest request,
        KitmsNotice kitmsNotice,
        List<MultipartFile> multipartFileList,
        List<Long> deleteAttachNoList
    ) throws IOException {
        CustomReturnDTO result = new CustomReturnDTO();
        Long noticeNo = kitmsNotice.getNoticeNo();
        String noticeTitle = kitmsNotice.getNoticeTitle();
        String noticeContent = kitmsNotice.getNoticeContent();

        result.setStatus(HttpStatus.BAD_REQUEST);
        if (noticeNo == null || noticeNo == 0) {
            result.setMessage("공지사항 번호는 필수값입니다.");
            return result;
        }
        if (noticeTitle == null || noticeTitle.isEmpty() || noticeTitle.isBlank()) {
            result.setMessage("공지사항 제목은 필수값입니다.");
            return result;
        }
        if (noticeContent == null || noticeContent.isEmpty() || noticeContent.isBlank()) {
            result.setMessage("공지사항 내용은 필수값입니다.");
            return result;
        }

        Optional<KitmsNotice> resultInfo = kitmsNoticeRepository.findById(noticeNo);

        if (resultInfo == null || resultInfo.isEmpty()) {
            result.setMessage("일치하는 정보가 없습니다.");
            return result;
        }

        resultInfo
            .map(existingKitmsNotice -> {
                if (kitmsNotice.getNoticeTitle() != null) {
                    existingKitmsNotice.setNoticeTitle(kitmsNotice.getNoticeTitle());
                }
                if (kitmsNotice.getNoticeContent() != null) {
                    existingKitmsNotice.setNoticeContent(kitmsNotice.getNoticeContent());
                }
                if (kitmsNotice.getStaticFlag() != null) {
                    existingKitmsNotice.setStaticFlag(kitmsNotice.getStaticFlag());
                }
                return existingKitmsNotice;
            })
            .map(kitmsNoticeRepository::save);

        fileService.updateNewAndDeleteFile(request, "kitms_notice", noticeNo, multipartFileList, deleteAttachNoList);

        result.setStatus(HttpStatus.OK);
        result.addColumn("resultInfo", resultInfo);

        return result;
    }

    /**
     * JWT 토큰 없이 공지사항 수정 (관리자용)
     */
    public CustomReturnDTO updateNoticeWithoutToken(
        KitmsNotice kitmsNotice,
        List<MultipartFile> multipartFileList,
        List<Long> deleteAttachNoList
    ) {
        CustomReturnDTO result = new CustomReturnDTO();
        Long noticeNo = kitmsNotice.getNoticeNo();
        String noticeTitle = kitmsNotice.getNoticeTitle();
        String noticeContent = kitmsNotice.getNoticeContent();

        result.setStatus(HttpStatus.BAD_REQUEST);
        if (noticeNo == null || noticeNo == 0) {
            result.setMessage("공지사항 번호는 필수값입니다.");
            return result;
        }
        if (noticeTitle == null || noticeTitle.isEmpty() || noticeTitle.isBlank()) {
            result.setMessage("공지사항 제목은 필수값입니다.");
            return result;
        }
        if (noticeContent == null || noticeContent.isEmpty() || noticeContent.isBlank()) {
            result.setMessage("공지사항 내용은 필수값입니다.");
            return result;
        }

        Optional<KitmsNotice> resultInfo = kitmsNoticeRepository.findById(noticeNo);

        if (resultInfo == null || resultInfo.isEmpty()) {
            result.setMessage("일치하는 공지사항이 없습니다.");
            return result;
        }

        resultInfo
            .map(existingKitmsNotice -> {
                if (kitmsNotice.getNoticeTitle() != null) {
                    existingKitmsNotice.setNoticeTitle(kitmsNotice.getNoticeTitle());
                }
                if (kitmsNotice.getNoticeContent() != null) {
                    existingKitmsNotice.setNoticeContent(kitmsNotice.getNoticeContent());
                }
                if (kitmsNotice.getStaticFlag() != null) {
                    existingKitmsNotice.setStaticFlag(kitmsNotice.getStaticFlag());
                }
                return existingKitmsNotice;
            })
            .map(kitmsNoticeRepository::save);

        // 파일 업로드는 선택사항이므로 null 체크
        if (multipartFileList != null && !multipartFileList.isEmpty()) {
            try {
                fileService.updateNewAndDeleteFile(null, "kitms_notice", noticeNo, multipartFileList, deleteAttachNoList);
            } catch (IOException e) {
                log.warn("파일 업로드 중 오류 발생: {}", e.getMessage());
            }
        }

        result.setStatus(HttpStatus.OK);
        result.addColumn("resultInfo", resultInfo);

        return result;
    }

    public CustomReturnDTO deleteNotice(Long noticeNo) {
        CustomReturnDTO result = new CustomReturnDTO();

        result.setStatus(HttpStatus.BAD_REQUEST);
        Optional<KitmsNotice> resultInfo = kitmsNoticeRepository.findById(noticeNo);

        if (resultInfo == null || resultInfo.isEmpty()) {
            result.setMessage("일치하는 정보가 없습니다.");
            return result;
        }

        fileService.removeFile("kitms_notice", noticeNo);
        kitmsNoticeRepository.deleteById(noticeNo);

        result.setStatus(HttpStatus.OK);
        result.addColumn("resultInfo", resultInfo);

        return result;
    }

    public CustomReturnDTO getNoticeAllList(KitmsCommonParamVO kitmsCommonParamVO) {
        try {
            log.info("공지사항 목록 조회 시작 - page: {}, size: {}, searchColumn: {}, searchValue: {}", 
                kitmsCommonParamVO.getPage(), kitmsCommonParamVO.getSize(), 
                kitmsCommonParamVO.getSearchColumn(), kitmsCommonParamVO.getSearchValue());
            
            // 파라미터 기본값 설정
            if (kitmsCommonParamVO.getPage() <= 0) {
                kitmsCommonParamVO.setPage(1);
            }
            // size가 0 이하이거나 6이 아닌 경우 6으로 설정
            if (kitmsCommonParamVO.getSize() <= 0 || kitmsCommonParamVO.getSize() != 6) {
                kitmsCommonParamVO.setSize(6);
            }
            
            // start, end 값 명시적 설정
            kitmsCommonParamVO.setStart((kitmsCommonParamVO.getPage() - 1) * kitmsCommonParamVO.getSize() + 1);
            kitmsCommonParamVO.setEnd(kitmsCommonParamVO.getPage() * kitmsCommonParamVO.getSize());
            
            log.info("페이징 파라미터 설정 완료 - page: {}, size: {}, start: {}, end: {}", 
                kitmsCommonParamVO.getPage(), kitmsCommonParamVO.getSize(), 
                kitmsCommonParamVO.getStart(), kitmsCommonParamVO.getEnd());
            
            // 모든 공지사항을 상단고정 우선으로 정렬해서 조회
            List<KitmsNoticeVO> resultList = kitmsNoticeMapper.getNoticeAllList(kitmsCommonParamVO);
            log.info("공지사항 조회 완료 - 개수: {}", resultList != null ? resultList.size() : 0);
            
            // 총 개수 계산
            int totalCount = 0;
            if (resultList != null && !resultList.isEmpty()) {
                // 첫 번째 항목에서 totalCount 가져오기
                KitmsNoticeVO firstItem = resultList.get(0);
                if (firstItem.getTotalCount() != null && !firstItem.getTotalCount().isEmpty()) {
                    try {
                        totalCount = Integer.parseInt(firstItem.getTotalCount());
                    } catch (NumberFormatException e) {
                        totalCount = resultList.size();
                    }
                } else {
                    totalCount = resultList.size();
                }
            }
            
            PagingListDTO paging = new PagingListDTO(
                resultList != null ? resultList : new ArrayList<>(),
                kitmsCommonParamVO.getPage(),
                kitmsCommonParamVO.getSize(),
                totalCount
            );
            
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.OK);
            result.addColumn("list", paging.getList());
            result.addColumn("page", paging.getPage());
            
            log.info("공지사항 목록 조회 완료 - 총 개수: {}, 페이지 정보: {}", 
                paging.getList().size(), paging.getPage());
            
            return result;
            
        } catch (Exception e) {
            log.error("공지사항 목록 조회 중 오류 발생", e);
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("공지사항을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }

    /**
     * 상단고정 공지사항만 가져오기 (메인 페이지용)
     */
    public CustomReturnDTO getStaticNoticeList(KitmsCommonParamVO kitmsCommonParamVO) {
        kitmsCommonParamVO.setNoticeStaticCount(noticeStaticCount);
        List<KitmsNoticeVO> noticeStaticList = kitmsNoticeMapper.getNoticeStaticList(kitmsCommonParamVO);
        
        CustomReturnDTO result = new CustomReturnDTO();
        result.setStatus(HttpStatus.OK);
        result.addColumn("list", noticeStaticList);
        result.addColumn("page", new PagingListDTO(
            noticeStaticList,
            kitmsCommonParamVO.getPage(),
            kitmsCommonParamVO.getSize(),
            noticeStaticList.size()
        ).getPage());
        return result;
    }

    public CustomReturnDTO searchKitmsNotice(Long noticeNo) {
        try {
            Optional<KitmsNotice> noticeOpt = kitmsNoticeRepository.findById(noticeNo);
            
            if (noticeOpt.isEmpty()) {
                CustomReturnDTO result = new CustomReturnDTO();
                result.setStatus(HttpStatus.NOT_FOUND);
                result.setMessage("공지사항을 찾을 수 없습니다.");
                return result;
            }
            
            KitmsNotice notice = noticeOpt.get();
            
            // 사용자 이름 조회 (간단한 방법으로)
            String userName = "관리자"; // 기본값, 필요시 별도 조회 로직 추가
            
            KitmsNoticeDTO resultInfo = new KitmsNoticeDTO(notice, userName);
            resultInfo.setAttachList(kitmsAttachRepository.findAllByAttachTableNameAndAttachTablePk(TABLE_NAME, noticeNo));

            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.OK);
            result.addColumn("resultInfo", resultInfo);
            return result;
            
        } catch (Exception e) {
            log.error("공지사항 조회 중 오류 발생", e);
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("공지사항 조회 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }
}
