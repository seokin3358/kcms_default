package com.kone.kitms.web.rest;

import com.kone.kitms.domain.KitmsNewsroom;
import com.kone.kitms.service.KitmsNewsroomService;
import com.kone.kitms.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * KITMS 보도자료(뉴스룸) 관리 REST API 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 보도자료(뉴스룸) 관리를 위한 모든 기능을 제공합니다:
 * - 보도자료 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 활성 상태의 보도자료 목록 조회 (공개용)
 * - 보도자료 제목으로 검색
 * - 최근 보도자료 목록 조회
 * - 프론트엔드용 표준 응답 형식 제공
 * - 페이징 처리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api")
public class KitmsNewsroomResource {

    private final Logger log = LoggerFactory.getLogger(KitmsNewsroomResource.class);

    private static final String ENTITY_NAME = "kitmsNewsroom";

    private final KitmsNewsroomService kitmsNewsroomService;

    public KitmsNewsroomResource(KitmsNewsroomService kitmsNewsroomService) {
        this.kitmsNewsroomService = kitmsNewsroomService;
    }

    /**
     * 보도자료 생성
     */
    @PostMapping("/newsrooms")
    public ResponseEntity<KitmsNewsroom> createNewsroom(@Valid @RequestBody KitmsNewsroom kitmsNewsroom) throws URISyntaxException {
        log.debug("REST request to save KitmsNewsroom : {}", kitmsNewsroom);
        
        if (kitmsNewsroom.getNewsroomNo() != null) {
            throw new BadRequestAlertException("A new newsroom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        KitmsNewsroom result = kitmsNewsroomService.save(kitmsNewsroom);
        
        return ResponseEntity.created(new URI("/api/newsrooms/" + result.getNewsroomNo()))
                .body(result);
    }

    /**
     * 보도자료 수정
     */
    @PutMapping("/newsrooms/{id}")
    public ResponseEntity<KitmsNewsroom> updateNewsroom(@PathVariable Long id, @Valid @RequestBody KitmsNewsroom kitmsNewsroom) {
        log.debug("REST request to update KitmsNewsroom : {}", kitmsNewsroom);
        
        if (kitmsNewsroom.getNewsroomNo() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        if (!id.equals(kitmsNewsroom.getNewsroomNo())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!kitmsNewsroomService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        KitmsNewsroom result = kitmsNewsroomService.save(kitmsNewsroom);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 모든 보도자료 조회 (관리자용)
     */
    @GetMapping("/newsrooms")
    public ResponseEntity<Page<KitmsNewsroom>> getAllNewsrooms(Pageable pageable) {
        log.debug("REST request to get a page of KitmsNewsrooms");
        
        Page<KitmsNewsroom> page = kitmsNewsroomService.findAll(pageable);
        
        return ResponseEntity.ok(page);
    }

    /**
     * 활성 상태의 보도자료 목록 조회 (공개용)
     */
    @GetMapping("/newsrooms/public")
    public ResponseEntity<Page<KitmsNewsroom>> getActiveNewsrooms(Pageable pageable) {
        log.debug("REST request to get active KitmsNewsrooms");
        
        Page<KitmsNewsroom> page = kitmsNewsroomService.findActiveNewsrooms(pageable);
        
        return ResponseEntity.ok(page);
    }

    /**
     * 보도자료 ID로 조회
     */
    @GetMapping("/newsrooms/{id}")
    public ResponseEntity<KitmsNewsroom> getNewsroom(@PathVariable Long id) {
        log.debug("REST request to get KitmsNewsroom : {}", id);
        
        Optional<KitmsNewsroom> kitmsNewsroom = kitmsNewsroomService.findOne(id);
        
        if (kitmsNewsroom.isPresent()) {
            return ResponseEntity.ok(kitmsNewsroom.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 보도자료 삭제
     */
    @DeleteMapping("/newsrooms/{id}")
    public ResponseEntity<Void> deleteNewsroom(@PathVariable Long id) {
        log.debug("REST request to delete KitmsNewsroom : {}", id);
        
        kitmsNewsroomService.delete(id);
        
        return ResponseEntity.noContent().build();
    }

    /**
     * 보도자료 제목으로 검색
     */
    @GetMapping("/newsrooms/search/title")
    public ResponseEntity<Page<KitmsNewsroom>> searchNewsroomsByTitle(@RequestParam String title, Pageable pageable) {
        log.debug("REST request to search KitmsNewsrooms by title: {}", title);
        
        Page<KitmsNewsroom> page = kitmsNewsroomService.searchByTitle(title, pageable);
        
        return ResponseEntity.ok(page);
    }

    /**
     * 최근 보도자료 목록 조회
     */
    @GetMapping("/newsrooms/recent")
    public ResponseEntity<List<KitmsNewsroom>> getRecentNewsrooms(@RequestParam(defaultValue = "5") int size) {
        log.debug("REST request to get recent KitmsNewsrooms");
        
        List<KitmsNewsroom> newsrooms = kitmsNewsroomService.findRecentNewsrooms(
            org.springframework.data.domain.PageRequest.of(0, size)
        );
        
        return ResponseEntity.ok(newsrooms);
    }

    /**
     * 활성 상태의 보도자료 목록 조회 (프론트엔드용 - 표준 응답 형식)
     */
    @GetMapping("/newsroom/list")
    public ResponseEntity<Map<String, Object>> getNewsroomList(Pageable pageable) {
        log.debug("REST request to get newsroom list for frontend");
        
        Page<KitmsNewsroom> page = kitmsNewsroomService.findActiveNewsrooms(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 200);
        response.put("message", "Success");
        
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getContent());
        data.put("totalElements", page.getTotalElements());
        data.put("totalPages", page.getTotalPages());
        data.put("currentPage", page.getNumber());
        data.put("size", page.getSize());
        data.put("hasNext", page.hasNext());
        data.put("hasPrevious", page.hasPrevious());
        
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }
}
