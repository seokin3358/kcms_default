package com.kone.kitms.service;

import com.kone.kitms.domain.KitmsNewsroom;
import com.kone.kitms.repository.KitmsNewsroomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * KITMS 보도자료(뉴스룸) 관리 서비스 클래스
 * 
 * 이 클래스는 KITMS 시스템의 보도자료(뉴스룸) 관리를 위한 비즈니스 로직을 제공합니다:
 * - 보도자료 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 활성 상태의 보도자료 목록 조회
 * - 보도자료 제목으로 검색
 * - 최근 보도자료 목록 조회
 * - 상태별 보도자료 개수 조회
 * - 페이징 처리
 * - 생성/수정 일시 및 사용자 추적
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Service
@Transactional
public class KitmsNewsroomService {

    private final Logger log = LoggerFactory.getLogger(KitmsNewsroomService.class);

    private final KitmsNewsroomRepository kitmsNewsroomRepository;

    public KitmsNewsroomService(KitmsNewsroomRepository kitmsNewsroomRepository) {
        this.kitmsNewsroomRepository = kitmsNewsroomRepository;
    }

    /**
     * 보도자료 저장
     */
    public KitmsNewsroom save(KitmsNewsroom kitmsNewsroom) {
        log.debug("Request to save KitmsNewsroom : {}", kitmsNewsroom);
        
        if (kitmsNewsroom.getNewsroomNo() == null) {
            // 새로 생성하는 경우
            kitmsNewsroom.setCreatedAt(ZonedDateTime.now());
            kitmsNewsroom.setCreatedBy("admin"); // TODO: 실제 사용자 정보로 변경
        } else {
            // 수정하는 경우
            kitmsNewsroom.setUpdatedAt(ZonedDateTime.now());
            kitmsNewsroom.setUpdatedBy("admin"); // TODO: 실제 사용자 정보로 변경
        }
        
        
        return kitmsNewsroomRepository.save(kitmsNewsroom);
    }

    /**
     * 모든 보도자료 조회
     */
    @Transactional(readOnly = true)
    public Page<KitmsNewsroom> findAll(Pageable pageable) {
        log.debug("Request to get all KitmsNewsrooms");
        return kitmsNewsroomRepository.findAll(pageable);
    }

    /**
     * 활성 상태의 보도자료 목록 조회 (생성일순)
     */
    @Transactional(readOnly = true)
    public Page<KitmsNewsroom> findActiveNewsrooms(Pageable pageable) {
        log.debug("Request to get active KitmsNewsrooms");
        return kitmsNewsroomRepository.findActiveNewsroomsOrderByCreatedAtDesc(pageable);
    }

    /**
     * 보도자료 ID로 조회
     */
    @Transactional(readOnly = true)
    public Optional<KitmsNewsroom> findOne(Long id) {
        log.debug("Request to get KitmsNewsroom : {}", id);
        return kitmsNewsroomRepository.findById(id);
    }

    /**
     * 보도자료 삭제
     */
    public void delete(Long id) {
        log.debug("Request to delete KitmsNewsroom : {}", id);
        kitmsNewsroomRepository.deleteById(id);
    }

    /**
     * 보도자료 제목으로 검색
     */
    @Transactional(readOnly = true)
    public Page<KitmsNewsroom> searchByTitle(String title, Pageable pageable) {
        log.debug("Request to search KitmsNewsrooms by title: {}", title);
        return kitmsNewsroomRepository.findByTitleContaining(title, pageable);
    }


    /**
     * 최근 보도자료 목록 조회
     */
    @Transactional(readOnly = true)
    public List<KitmsNewsroom> findRecentNewsrooms(Pageable pageable) {
        log.debug("Request to get recent KitmsNewsrooms");
        return kitmsNewsroomRepository.findRecentNewsrooms(pageable);
    }


    /**
     * 상태별 보도자료 개수 조회
     */
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        log.debug("Request to count KitmsNewsrooms by status: {}", status);
        return kitmsNewsroomRepository.countByStatus(status);
    }

    /**
     * 전체 보도자료 개수 조회
     */
    @Transactional(readOnly = true)
    public long countAll() {
        log.debug("Request to count all KitmsNewsrooms");
        return kitmsNewsroomRepository.countAllNewsrooms();
    }
}
