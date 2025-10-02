package com.kone.kitms.repository;

import com.kone.kitms.domain.AdminMenuPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 관리자 메뉴 권한 리포지토리
 */
@Repository
public interface AdminMenuPermissionRepository extends JpaRepository<AdminMenuPermission, Long> {

    /**
     * 사용자 ID로 권한 목록 조회
     */
    List<AdminMenuPermission> findByUserIdAndIsGrantedTrue(String userId);

    /**
     * 사용자 ID와 메뉴 번호로 권한 조회
     */
    Optional<AdminMenuPermission> findByUserIdAndMenuNo(String userId, Long menuNo);

    /**
     * 사용자 ID로 모든 권한 삭제
     */
    void deleteByUserId(String userId);

    /**
     * 사용자 ID로 권한이 있는 메뉴 번호 목록 조회
     */
    @Query("SELECT amp.menuNo FROM AdminMenuPermission amp WHERE amp.userId = :userId AND amp.isGranted = true")
    List<Long> findMenuNosByUserIdAndIsGrantedTrue(@Param("userId") String userId);

    /**
     * 메뉴 번호로 권한이 있는 사용자 ID 목록 조회
     */
    @Query("SELECT amp.userId FROM AdminMenuPermission amp WHERE amp.menuNo = :menuNo AND amp.isGranted = true")
    List<String> findUserIdsByMenuNoAndIsGrantedTrue(@Param("menuNo") Long menuNo);

    /**
     * 사용자 ID로 권한 목록 조회 (권한 여부 포함)
     */
    List<AdminMenuPermission> findByUserId(String userId);
}
