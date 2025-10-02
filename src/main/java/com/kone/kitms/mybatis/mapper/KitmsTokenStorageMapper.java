package com.kone.kitms.mybatis.mapper;

import com.kone.kitms.mybatis.vo.KitmsTokenStorageVO;
import com.kone.kitms.mybatis.vo.KitmsTokenUserVO;

import org.springframework.stereotype.Repository;

/**
 * KITMS 토큰 저장소 매퍼 인터페이스
 * 
 * 이 인터페이스는 KITMS 시스템의 토큰 저장소 관리를 위한 MyBatis 매퍼입니다:
 * - 사용자 토큰 저장 및 조회
 * - 토큰 생성, 수정, 삭제
 * - 사용자별 토큰 정보 관리
 * - 토큰 기반 사용자 인증 지원
 * - 세션 관리 및 보안 강화
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface KitmsTokenStorageMapper {
    KitmsTokenStorageVO getStorageToken(String userId);
    int insertStorageToken(String userId, String userToken);
    int updateStorageToken(String userId, String userToken);
    int deleteStorageToken(String userId);

    KitmsTokenUserVO getUserInfoForToken(String userId);
}
