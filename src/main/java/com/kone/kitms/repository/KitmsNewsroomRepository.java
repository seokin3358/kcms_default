package com.kone.kitms.repository;

import com.kone.kitms.domain.KitmsNewsroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 보도자료(뉴스룸) 리포지토리
 */
@Repository
public interface KitmsNewsroomRepository extends JpaRepository<KitmsNewsroom, Long> {

    /**
     * 활성 상태의 보도자료 목록 조회 (생성일순)
     */
    @Query("SELECT n FROM KitmsNewsroom n WHERE n.newsroomStatus = 'ACTIVE' ORDER BY n.createdAt DESC")
    Page<KitmsNewsroom> findActiveNewsroomsOrderByCreatedAtDesc(Pageable pageable);

    /**
     * 제목으로 보도자료 검색
     */
    @Query("SELECT n FROM KitmsNewsroom n WHERE n.newsroomStatus = 'ACTIVE' AND n.newsroomTitle LIKE %:title% ORDER BY n.createdAt DESC")
    Page<KitmsNewsroom> findByTitleContaining(@Param("title") String title, Pageable pageable);

    /**
     * 최근 보도자료 목록 조회 (지정된 개수만큼)
     */
    @Query("SELECT n FROM KitmsNewsroom n WHERE n.newsroomStatus = 'ACTIVE' ORDER BY n.createdAt DESC")
    List<KitmsNewsroom> findRecentNewsrooms(Pageable pageable);

    /**
     * 상태별 보도자료 개수 조회
     */
    @Query("SELECT COUNT(n) FROM KitmsNewsroom n WHERE n.newsroomStatus = :status")
    long countByStatus(@Param("status") String status);

    /**
     * 전체 보도자료 개수 조회
     */
    @Query("SELECT COUNT(n) FROM KitmsNewsroom n")
    long countAllNewsrooms();
}
