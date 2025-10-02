package com.kone.kitms.mybatis.mapper;

import com.kone.kitms.service.dto.KitmsUserDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * KITMS 사용자 매퍼 인터페이스
 * 
 * 이 인터페이스는 KITMS 시스템의 사용자 관리를 위한 MyBatis 매퍼입니다:
 * - 사용자 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 계층적 조직 구조 기반 사용자 조회
 * - 사용자 검색 및 필터링
 * - 조직별 사용자 관리
 * - 사용자 이메일 및 ID 중복 검사
 * - 사용자 조직 이동 기능
 * - 페이징 처리 지원
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface KamsUserMapper {
    Long countUsersUnderHierarchy(
        @Param("groupNoList") Set<Long> groupNoList,
        @Param("corpNoList") Set<Long> corpNoList,
        @Param("headqNoList") Set<Long> headqNoList,
        @Param("teamNoList") Set<Long> teamNoList
    );

    List<KitmsUserDTO> getAllUser(
        @Param("searchValue") String searchValue,
        @Param("authCode") String authCode,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    int getAllUserTotal(@Param("searchValue") String searchValue, @Param("authCode") String authCode);
    KitmsUserDTO getUser(@Param("id") Long id);
    void createUser(KitmsUserDTO dto);
    Optional<KitmsUserDTO> getUserByUserNo(@Param("userNo") Long userNo);
    void updateUser(KitmsUserDTO dto);
    void deleteUser(@Param("userNoList") List<Long> userNoList);
    int checkOrganMember(@Param("code") String code);
    void moveUser(@Param("userNo") Long userNo, @Param("organNo") Long organNo);
    int existEmailChk(@Param("userEmail") String userEmail, @Param("userNo") Long userNo);

    int existUserId(String userId);
    int existUserNo(String userNo);
    Long selectUserNoByUserId(@Param("userId") String userId);
}
