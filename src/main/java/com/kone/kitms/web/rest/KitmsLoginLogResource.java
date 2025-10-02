package com.kone.kitms.web.rest;

import com.kone.kitms.domain.KitmsLoginLog;
import com.kone.kitms.repository.KitmsLoginLogRepository;
import com.kone.kitms.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * KITMS 로그인 로그 관리 REST API 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 사용자 로그인 로그 관리를 위한 모든 기능을 제공합니다:
 * - 로그인 로그 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 로그인 성공/실패 기록 관리
 * - 로그인 IP 및 시간 추적
 * - 로그인 실패 사유 기록
 * - 로그인 로그 조회 및 분석
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/kitms-login-logs")
@Transactional
public class KitmsLoginLogResource {

    private final Logger log = LoggerFactory.getLogger(KitmsLoginLogResource.class);

    private static final String ENTITY_NAME = "kitmsLoginLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KitmsLoginLogRepository kitmsLoginLogRepository;

    public KitmsLoginLogResource(KitmsLoginLogRepository kitmsLoginLogRepository) {
        this.kitmsLoginLogRepository = kitmsLoginLogRepository;
    }

    /**
     * {@code POST  /kitms-login-logs} : Create a new kitmsLoginLog.
     *
     * @param kitmsLoginLog the kitmsLoginLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kitmsLoginLog, or with status {@code 400 (Bad Request)} if the kitmsLoginLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KitmsLoginLog> createKitmsLoginLog(@Valid @RequestBody KitmsLoginLog kitmsLoginLog) throws URISyntaxException {
        log.debug("REST request to save KitmsLoginLog : {}", kitmsLoginLog);
        if (kitmsLoginLog.getLogNo() != null) {
            throw new BadRequestAlertException("A new kitmsLoginLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KitmsLoginLog result = kitmsLoginLogRepository.save(kitmsLoginLog);
        return ResponseEntity
            .created(new URI("/api/kitms-login-logs/" + result.getLogNo()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getLogNo().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kitms-login-logs/:logNo} : Updates an existing kitmsLoginLog.
     *
     * @param logNo the id of the kitmsLoginLog to save.
     * @param kitmsLoginLog the kitmsLoginLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kitmsLoginLog,
     * or with status {@code 400 (Bad Request)} if the kitmsLoginLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kitmsLoginLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{logNo}")
    public ResponseEntity<KitmsLoginLog> updateKitmsLoginLog(
        @PathVariable(value = "logNo", required = false) final Long logNo,
        @Valid @RequestBody KitmsLoginLog kitmsLoginLog
    ) throws URISyntaxException {
        log.debug("REST request to update KitmsLoginLog : {}, {}", logNo, kitmsLoginLog);
        if (kitmsLoginLog.getLogNo() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(logNo, kitmsLoginLog.getLogNo())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kitmsLoginLogRepository.existsById(logNo)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        KitmsLoginLog result = kitmsLoginLogRepository.save(kitmsLoginLog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kitmsLoginLog.getLogNo().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /kitms-login-logs/:logNo} : Partial updates given fields of an existing kitmsLoginLog, field will ignore if it is null
     *
     * @param logNo the id of the kitmsLoginLog to save.
     * @param kitmsLoginLog the kitmsLoginLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kitmsLoginLog,
     * or with status {@code 400 (Bad Request)} if the kitmsLoginLog is not valid,
     * or with status {@code 404 (Not Found)} if the kitmsLoginLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the kitmsLoginLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{logNo}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KitmsLoginLog> partialUpdateKitmsLoginLog(
        @PathVariable(value = "logNo", required = false) final Long logNo,
        @NotNull @RequestBody KitmsLoginLog kitmsLoginLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update KitmsLoginLog partially : {}, {}", logNo, kitmsLoginLog);
        if (kitmsLoginLog.getLogNo() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(logNo, kitmsLoginLog.getLogNo())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kitmsLoginLogRepository.existsById(logNo)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KitmsLoginLog> result = kitmsLoginLogRepository
            .findById(kitmsLoginLog.getLogNo())
            .map(existingKitmsLoginLog -> {
                if (kitmsLoginLog.getUserId() != null) {
                    existingKitmsLoginLog.setUserId(kitmsLoginLog.getUserId());
                }
                if (kitmsLoginLog.getLoginSuccess() != null) {
                    existingKitmsLoginLog.setLoginSuccess(kitmsLoginLog.getLoginSuccess());
                }
                if (kitmsLoginLog.getLoginDt() != null) {
                    existingKitmsLoginLog.setLoginDt(kitmsLoginLog.getLoginDt());
                }
                if (kitmsLoginLog.getLoginIp() != null) {
                    existingKitmsLoginLog.setLoginIp(kitmsLoginLog.getLoginIp());
                }
                if (kitmsLoginLog.getLoginReason() != null) {
                    existingKitmsLoginLog.setLoginReason(kitmsLoginLog.getLoginReason());
                }

                return existingKitmsLoginLog;
            })
            .map(kitmsLoginLogRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kitmsLoginLog.getLogNo().toString())
        );
    }

    /**
     * {@code GET  /kitms-login-logs} : get all the kitmsLoginLogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kitmsLoginLogs in body.
     */
    @GetMapping("")
    public List<KitmsLoginLog> getAllKitmsLoginLogs() {
        log.debug("REST request to get all KitmsLoginLogs");
        return kitmsLoginLogRepository.findAll();
    }

    /**
     * {@code GET  /kitms-login-logs/:id} : get the "id" kitmsLoginLog.
     *
     * @param id the id of the kitmsLoginLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kitmsLoginLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KitmsLoginLog> getKitmsLoginLog(@PathVariable("id") Long id) {
        log.debug("REST request to get KitmsLoginLog : {}", id);
        Optional<KitmsLoginLog> kitmsLoginLog = kitmsLoginLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(kitmsLoginLog);
    }

    /**
     * {@code DELETE  /kitms-login-logs/:id} : delete the "id" kitmsLoginLog.
     *
     * @param id the id of the kitmsLoginLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKitmsLoginLog(@PathVariable("id") Long id) {
        log.debug("REST request to delete KitmsLoginLog : {}", id);
        kitmsLoginLogRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
