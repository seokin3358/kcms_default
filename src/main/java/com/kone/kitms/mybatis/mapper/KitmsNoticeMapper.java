package com.kone.kitms.mybatis.mapper;

import com.kone.kitms.mybatis.vo.KitmsCommonParamVO;
import com.kone.kitms.mybatis.vo.KitmsNoticeVO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * KITMS 공지사항 매퍼 인터페이스
 * 
 * 이 인터페이스는 KITMS 시스템의 공지사항 관리를 위한 MyBatis 매퍼입니다:
 * - 공지사항 목록 조회
 * - 고정 공지사항 조회
 * - 전체 공지사항 조회
 * - 공지사항 검색 및 필터링
 * - 페이징 처리 지원
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface KitmsNoticeMapper {
    List<KitmsNoticeVO> getNoticeStaticList(KitmsCommonParamVO kitmsCommonParamVO);
    List<KitmsNoticeVO> getNoticeAllList(KitmsCommonParamVO kitmsCommonParamVO);
}
