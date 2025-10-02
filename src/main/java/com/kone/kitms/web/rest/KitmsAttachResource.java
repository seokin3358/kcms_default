package com.kone.kitms.web.rest;

import com.kone.kitms.domain.KitmsAttach;
import com.kone.kitms.repository.KitmsAttachRepository;
import com.kone.kitms.security.FileSecurityValidator;
import com.kone.kitms.web.rest.errors.BadRequestAlertException;
import com.kone.kitms.service.dto.CustomReturnDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * KITMS 첨부파일 관리 REST API 컨트롤러
 * 
 * 이 클래스는 KITMS 시스템의 첨부파일 관리를 위한 모든 기능을 제공합니다:
 * - 첨부파일 CRUD 작업 (생성, 조회, 수정, 삭제)
 * - 이미지 파일 업로드 및 관리
 * - 일반 파일 업로드 및 다운로드
 * - 썸네일 설정 및 관리
 * - 파일 검색 및 목록 조회
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/kitms-attaches")
@Transactional
public class KitmsAttachResource {

    private final Logger log = LoggerFactory.getLogger(KitmsAttachResource.class);

    private static final String ENTITY_NAME = "kitmsAttach";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${app.upload.path:./images}")
    private String uploadPath;

    @Autowired
    private FileSecurityValidator fileSecurityValidator;

    private final KitmsAttachRepository kitmsAttachRepository;

    public KitmsAttachResource(KitmsAttachRepository kitmsAttachRepository) {
        this.kitmsAttachRepository = kitmsAttachRepository;
    }

    /**
     * 새로운 첨부파일 정보 생성
     * 
     * 데이터베이스에 새로운 첨부파일 정보를 생성합니다.
     * 
     * @param kitmsAttach 생성할 첨부파일 정보
     * @return 생성된 첨부파일 정보와 함께 201 상태코드, 또는 ID가 이미 존재하는 경우 400 상태코드
     * @throws URISyntaxException URI 구문이 잘못된 경우
     */
    @PostMapping("")
    public ResponseEntity<KitmsAttach> createKitmsAttach(@Valid @RequestBody KitmsAttach kitmsAttach) throws URISyntaxException {
        log.debug("REST request to save KitmsAttach : {}", kitmsAttach);
        if (kitmsAttach.getAttachNo() != null) {
            throw new BadRequestAlertException("A new kitmsAttach cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KitmsAttach result = kitmsAttachRepository.save(kitmsAttach);
        return ResponseEntity
            .created(new URI("/api/kitms-attaches/" + result.getAttachNo()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getAttachNo().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kitms-attaches/:attachNo} : Updates an existing kitmsAttach.
     *
     * @param attachNo the id of the kitmsAttach to save.
     * @param kitmsAttach the kitmsAttach to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kitmsAttach,
     * or with status {@code 400 (Bad Request)} if the kitmsAttach is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kitmsAttach couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{attachNo}")
    public ResponseEntity<KitmsAttach> updateKitmsAttach(
        @PathVariable(value = "attachNo", required = false) final Long attachNo,
        @Valid @RequestBody KitmsAttach kitmsAttach
    ) throws URISyntaxException {
        log.debug("REST request to update KitmsAttach : {}, {}", attachNo, kitmsAttach);
        if (kitmsAttach.getAttachNo() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(attachNo, kitmsAttach.getAttachNo())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kitmsAttachRepository.existsById(attachNo)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        KitmsAttach result = kitmsAttachRepository.save(kitmsAttach);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kitmsAttach.getAttachNo().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /kitms-attaches/:attachNo} : Partial updates given fields of an existing kitmsAttach, field will ignore if it is null
     *
     * @param attachNo the id of the kitmsAttach to save.
     * @param kitmsAttach the kitmsAttach to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kitmsAttach,
     * or with status {@code 400 (Bad Request)} if the kitmsAttach is not valid,
     * or with status {@code 404 (Not Found)} if the kitmsAttach is not found,
     * or with status {@code 500 (Internal Server Error)} if the kitmsAttach couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{attachNo}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KitmsAttach> partialUpdateKitmsAttach(
        @PathVariable(value = "attachNo", required = false) final Long attachNo,
        @NotNull @RequestBody KitmsAttach kitmsAttach
    ) throws URISyntaxException {
        log.debug("REST request to partial update KitmsAttach partially : {}, {}", attachNo, kitmsAttach);
        if (kitmsAttach.getAttachNo() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(attachNo, kitmsAttach.getAttachNo())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kitmsAttachRepository.existsById(attachNo)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KitmsAttach> result = kitmsAttachRepository
            .findById(kitmsAttach.getAttachNo())
            .map(existingKitmsAttach -> {
                if (kitmsAttach.getAttachTableName() != null) {
                    existingKitmsAttach.setAttachTableName(kitmsAttach.getAttachTableName());
                }
                if (kitmsAttach.getAttachTablePk() != null) {
                    existingKitmsAttach.setAttachTablePk(kitmsAttach.getAttachTablePk());
                }
                if (kitmsAttach.getAttachFilePath() != null) {
                    existingKitmsAttach.setAttachFilePath(kitmsAttach.getAttachFilePath());
                }
                if (kitmsAttach.getAttachFileName() != null) {
                    existingKitmsAttach.setAttachFileName(kitmsAttach.getAttachFileName());
                }
                if (kitmsAttach.getCreateDt() != null) {
                    existingKitmsAttach.setCreateDt(kitmsAttach.getCreateDt());
                }
                if (kitmsAttach.getCreateUserId() != null) {
                    existingKitmsAttach.setCreateUserId(kitmsAttach.getCreateUserId());
                }

                return existingKitmsAttach;
            })
            .map(kitmsAttachRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kitmsAttach.getAttachNo().toString())
        );
    }

    /**
     * {@code GET  /kitms-attaches} : get all the kitmsAttaches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kitmsAttaches in body.
     */
    @GetMapping("")
    public List<KitmsAttach> getAllKitmsAttaches() {
        log.debug("REST request to get all KitmsAttaches");
        return kitmsAttachRepository.findAll();
    }

    /**
     * {@code GET  /kitms-attaches/:id} : get the "id" kitmsAttach.
     *
     * @param id the id of the kitmsAttach to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kitmsAttach, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KitmsAttach> getKitmsAttach(@PathVariable("id") Long id) {
        log.debug("REST request to get KitmsAttach : {}", id);
        Optional<KitmsAttach> kitmsAttach = kitmsAttachRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(kitmsAttach);
    }


    /**
     * {@code POST  /kitms-attach/upload} : Upload image for notice
     *
     * @param file the image file to upload
     * @param noticeNo the notice number (optional for CKEditor)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the uploaded file info
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "noticeNo", required = false) String noticeNo
    ) {
        try {
            if (file.isEmpty()) {
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("파일이 비어있습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 보안 검증 - 파일 시그니처 및 확장자 검증
            String originalFilename = file.getOriginalFilename();
            FileSecurityValidator.ValidationResult validationResult = 
                fileSecurityValidator.validateImageFile(originalFilename, file.getInputStream());
            
            if (!validationResult.isValid()) {
                log.warn("이미지 파일 보안 검증 실패: {} - {}", originalFilename, validationResult.getErrorMessage());
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage(validationResult.getErrorMessage());
                return ResponseEntity.badRequest().body(response);
            }

            // 업로드 디렉토리 생성
            String uploadDirName = noticeNo != null ? "notice_" + noticeNo : "temp";
            String noticeDir = uploadPath + "/" + uploadDirName;
            Path uploadDir = Paths.get(noticeDir);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 파일명 생성 (UUID + 원본 확장자)
            String fileExtension = getFileExtension(originalFilename);
            String fileName = UUID.randomUUID().toString() + fileExtension;
            Path filePath = uploadDir.resolve(fileName);

            // 파일 저장
            Files.copy(file.getInputStream(), filePath);

            String fileUrl = "/images/" + uploadDirName + "/" + fileName;

            // CKEditor용 응답인지 확인 (noticeNo가 없으면 CKEditor용)
            if (noticeNo == null) {
                // CKEditor는 특별한 JSON 형식을 기대함
                Map<String, Object> ckResponse = new HashMap<>();
                ckResponse.put("url", fileUrl);
                return ResponseEntity.ok(ckResponse);
            }

            // 데이터베이스에 정보 저장 (일반 업로드용)
            KitmsAttach attach = new KitmsAttach();
            attach.setAttachTableName("KITMS_NOTICE");
            attach.setAttachTablePk(Long.parseLong(noticeNo));
            attach.setAttachFilePath(fileUrl);
            attach.setAttachFileName(originalFilename);
            attach.setAttachFileSize(file.getSize());
            attach.setCreateDt(ZonedDateTime.now());
            attach.setCreateUserId("admin");
            attach.setIsThumbnail(false);

            KitmsAttach savedAttach = kitmsAttachRepository.save(attach);

            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(HttpStatus.OK);
            response.setMessage("이미지가 성공적으로 업로드되었습니다.");
            Map<String, Object> data = new HashMap<>();
            data.put("attachNo", savedAttach.getAttachNo());
            data.put("fileName", savedAttach.getAttachFileName());
            data.put("filePath", savedAttach.getAttachFilePath());
            response.setData(data);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("파일 업로드 오류", e);
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("파일 업로드 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * {@code GET  /kitms-attach/notice/{noticeNo}} : Get all images for a notice
     *
     * @param noticeNo the notice number
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of images
     */
    @GetMapping("/notice/{noticeNo}")
    public ResponseEntity<CustomReturnDTO> getNoticeImages(@PathVariable("noticeNo") Long noticeNo) {
        try {
            List<KitmsAttach> images = kitmsAttachRepository.findByAttachTableNameAndAttachTablePk("KITMS_NOTICE", noticeNo);
            
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(HttpStatus.OK);
            Map<String, Object> data = new HashMap<>();
            data.put("list", images);
            response.setData(data);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("이미지 목록 조회 오류", e);
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("이미지 목록 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * {@code DELETE  /kitms-attach/{attachNo}} : Delete an attachment file
     *
     * @param attachNo the attachment number to delete
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and deletion result
     */
    @DeleteMapping("/{attachNo}")
    public ResponseEntity<CustomReturnDTO> deleteAttachment(@PathVariable("attachNo") Long attachNo) {
        try {
            log.info("첨부파일 삭제 요청: {}", attachNo);
            
            // 첨부파일 정보 조회
            Optional<KitmsAttach> attachOpt = kitmsAttachRepository.findById(attachNo);
            if (attachOpt.isEmpty()) {
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("첨부파일을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            KitmsAttach attach = attachOpt.get();
            String filePath = attach.getAttachFilePath();
            
            // 실제 파일 삭제
            try {
                if (filePath != null && !filePath.isEmpty()) {
                    // 웹 경로를 실제 파일 경로로 변환
                    String actualFilePath = uploadPath + filePath.replace("/images/", "/");
                    Path fileToDelete = Paths.get(actualFilePath);
                    
                    if (Files.exists(fileToDelete)) {
                        Files.delete(fileToDelete);
                        log.info("파일 삭제 완료: {}", actualFilePath);
                    } else {
                        log.warn("삭제할 파일이 존재하지 않음: {}", actualFilePath);
                    }
                }
            } catch (IOException e) {
                log.error("파일 삭제 중 오류 발생: {}", filePath, e);
                // 파일 삭제 실패해도 DB에서 제거는 진행
            }

            // 데이터베이스에서 첨부파일 정보 삭제
            kitmsAttachRepository.delete(attach);
            
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(HttpStatus.OK);
            response.setMessage("첨부파일이 성공적으로 삭제되었습니다.");
            
            Map<String, Object> data = new HashMap<>();
            data.put("attachNo", attachNo);
            data.put("fileName", attach.getAttachFileName());
            response.setData(data);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("첨부파일 삭제 오류", e);
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("첨부파일 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * {@code PUT  /kitms-attach/thumbnail/{attachNo}} : Set image as thumbnail
     *
     * @param attachNo the attach number
     * @param request the request containing noticeNo
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     */
    @PutMapping("/thumbnail/{attachNo}")
    public ResponseEntity<CustomReturnDTO> setThumbnail(
        @PathVariable("attachNo") Long attachNo,
        @RequestBody Map<String, Object> request
    ) {
        try {
            Long noticeNo = Long.parseLong(request.get("noticeNo").toString());

            // 해당 공지사항의 모든 이미지에서 썸네일 해제
            List<KitmsAttach> allImages = kitmsAttachRepository.findByAttachTableNameAndAttachTablePk("KITMS_NOTICE", noticeNo);
            for (KitmsAttach image : allImages) {
                image.setIsThumbnail(false);
                kitmsAttachRepository.save(image);
            }

            // 선택된 이미지를 썸네일로 설정
            Optional<KitmsAttach> attachOpt = kitmsAttachRepository.findById(attachNo);
            if (attachOpt.isPresent()) {
                KitmsAttach attach = attachOpt.get();
                attach.setIsThumbnail(true);
                kitmsAttachRepository.save(attach);

                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatus(HttpStatus.OK);
                response.setMessage("썸네일이 설정되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("이미지를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            log.error("썸네일 설정 오류", e);
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("썸네일 설정 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    private boolean isImageFile(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        return extension.equals(".jpg") || extension.equals(".jpeg") || 
               extension.equals(".png") || extension.equals(".gif") || 
               extension.equals(".bmp") || extension.equals(".webp");
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex) : "";
    }

    /**
     * {@code POST  /kitms-attaches/upload-file} : Upload any file for notice
     *
     * @param file the file to upload
     * @param tableName the table name (e.g., "KITMS_NOTICE")
     * @param tablePk the table primary key
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the uploaded file info
     */
    @PostMapping("/upload-file")
    public ResponseEntity<?> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("tableName") String tableName,
        @RequestParam("tablePk") String tablePk
    ) {
        log.info("첨부파일 업로드 요청 - 파일명: {}, 테이블명: {}, PK: {}", 
                file.getOriginalFilename(), tableName, tablePk);
        
        try {
            if (file.isEmpty()) {
                log.warn("빈 파일 업로드 시도");
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatusCode(400);
                response.setMessage("파일이 비어있습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 파일 크기 체크 (10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatusCode(400);
                response.setMessage("파일 크기가 너무 큽니다. 최대 10MB까지 업로드 가능합니다.");
                return ResponseEntity.badRequest().body(response);
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatusCode(400);
                response.setMessage("파일명이 올바르지 않습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 보안 검증 - 파일 시그니처 및 확장자 검증
            FileSecurityValidator.ValidationResult validationResult = 
                fileSecurityValidator.validateDocumentFile(originalFilename, file.getInputStream());
            
            if (!validationResult.isValid()) {
                log.warn("파일 보안 검증 실패: {} - {}", originalFilename, validationResult.getErrorMessage());
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatusCode(400);
                response.setMessage(validationResult.getErrorMessage());
                return ResponseEntity.badRequest().body(response);
            }

            // 임시 디렉토리 사용 (tablePk가 'temp'인 경우)
            String uploadDirName;
            if ("temp".equals(tablePk)) {
                uploadDirName = "temp_" + System.currentTimeMillis();
            } else {
                uploadDirName = tableName.toLowerCase() + "_" + tablePk;
            }
            
            String fileDir = uploadPath + "/" + uploadDirName;
            Path uploadDir = Paths.get(fileDir);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 파일명 생성 (UUID + 원본 확장자)
            String fileExtension = getFileExtension(originalFilename);
            String fileName = UUID.randomUUID().toString() + fileExtension;
            Path filePath = uploadDir.resolve(fileName);

            // 파일 저장
            Files.copy(file.getInputStream(), filePath);

            String fileUrl = "/images/" + uploadDirName + "/" + fileName;

            // 임시 파일인 경우 데이터베이스에 저장하지 않음
            if ("temp".equals(tablePk)) {
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatusCode(200);
                response.setMessage("파일이 성공적으로 업로드되었습니다.");
                Map<String, Object> data = new HashMap<>();
                data.put("fileName", fileName);
                data.put("filePath", fileUrl);
                data.put("fileSize", file.getSize());
                data.put("originalName", originalFilename);
                response.setData(data);

                return ResponseEntity.ok(response);
            }

            // 데이터베이스에 정보 저장 (기존 로직)
            Long tablePkLong;
            try {
                tablePkLong = Long.parseLong(tablePk);
            } catch (NumberFormatException e) {
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatusCode(400);
                response.setMessage("잘못된 테이블 PK 형식입니다.");
                return ResponseEntity.badRequest().body(response);
            }

            KitmsAttach attach = new KitmsAttach();
            attach.setAttachTableName(tableName);
            attach.setAttachTablePk(tablePkLong);
            attach.setAttachFilePath(fileUrl);
            attach.setAttachFileName(originalFilename);
            attach.setAttachFileSize(file.getSize());
            attach.setCreateDt(ZonedDateTime.now());
            attach.setCreateUserId("admin");
            attach.setIsThumbnail(false);

            KitmsAttach savedAttach = kitmsAttachRepository.save(attach);
            log.info("첨부파일 저장 완료 - attachNo: {}, 파일명: {}, 경로: {}", 
                    savedAttach.getAttachNo(), savedAttach.getAttachFileName(), savedAttach.getAttachFilePath());

            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatusCode(200);
            response.setMessage("파일이 성공적으로 업로드되었습니다.");
            Map<String, Object> data = new HashMap<>();
            data.put("attachNo", savedAttach.getAttachNo());
            data.put("fileName", savedAttach.getAttachFileName());
            data.put("filePath", savedAttach.getAttachFilePath());
            data.put("fileSize", savedAttach.getAttachFileSize());
            response.setData(data);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("파일 업로드 오류", e);
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatusCode(500);
            response.setMessage("파일 업로드 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * {@code GET  /kitms-attaches/list/{tableName}/{tablePk}} : Get all attachments for a table
     *
     * @param tableName the table name
     * @param tablePk the table primary key
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachments
     */
    @GetMapping("/list/{tableName}/{tablePk}")
    public ResponseEntity<?> getAttachments(
        @PathVariable("tableName") String tableName,
        @PathVariable("tablePk") Long tablePk
    ) {
        try {
            List<KitmsAttach> attachments = kitmsAttachRepository.findByAttachTableNameAndAttachTablePk(tableName, tablePk);
            
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatusCode(200);
            Map<String, Object> data = new HashMap<>();
            data.put("list", attachments);
            response.setData(data);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("첨부파일 목록 조회 오류", e);
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatusCode(500);
            response.setMessage("첨부파일 목록 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * {@code GET  /kitms-attaches/find-image/{fileName}} : Find image file by filename
     *
     * @param fileName the file name to search for
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the file path
     */
    @GetMapping("/find-image/{fileName}")
    public ResponseEntity<?> findImageFile(@PathVariable("fileName") String fileName) {
        try {
            // images 디렉토리에서 파일명으로 검색
            Path imagesDir = Paths.get(uploadPath);
            if (!Files.exists(imagesDir)) {
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatusCode(404);
                response.setMessage("이미지 디렉토리를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // 하위 디렉토리들을 순회하며 파일 찾기
            try (var stream = Files.walk(imagesDir)) {
                Optional<Path> foundFile = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .findFirst();

                if (foundFile.isPresent()) {
                    Path filePath = foundFile.get();
                    String relativePath = imagesDir.relativize(filePath).toString();
                    String fileUrl = "/images/" + relativePath.replace("\\", "/");
                    
                    CustomReturnDTO response = new CustomReturnDTO();
                    response.setStatusCode(200);
                    response.setMessage("파일을 찾았습니다.");
                    Map<String, Object> data = new HashMap<>();
                    data.put("filePath", fileUrl);
                    data.put("fileName", fileName);
                    response.setData(data);
                    
                    return ResponseEntity.ok(response);
                } else {
                    CustomReturnDTO response = new CustomReturnDTO();
                    response.setStatusCode(404);
                    response.setMessage("파일을 찾을 수 없습니다: " + fileName);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            }

        } catch (IOException e) {
            log.error("이미지 파일 검색 오류", e);
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatusCode(500);
            response.setMessage("파일 검색 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * {@code GET  /kitms-attaches/download/{attachNo}} : Download attachment file
     *
     * @param attachNo the attach number
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the file content
     */
    @GetMapping("/file-download/{attachNo}")
    public ResponseEntity<?> downloadFile(@PathVariable("attachNo") Long attachNo) {
        try {
            Optional<KitmsAttach> attachOpt = kitmsAttachRepository.findById(attachNo);
            if (attachOpt.isPresent()) {
                KitmsAttach attach = attachOpt.get();
                
                Path filePath = Paths.get("." + attach.getAttachFilePath());
                if (Files.exists(filePath)) {
                    byte[] fileContent = Files.readAllBytes(filePath);
                    
                    return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + attach.getAttachFileName() + "\"")
                        .header("Content-Type", "application/octet-stream")
                        .body(fileContent);
                } else {
                    CustomReturnDTO response = new CustomReturnDTO();
                    response.setStatusCode(404);
                    response.setMessage("파일을 찾을 수 없습니다.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                CustomReturnDTO response = new CustomReturnDTO();
                response.setStatusCode(404);
                response.setMessage("첨부파일을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            log.error("파일 다운로드 오류", e);
            CustomReturnDTO response = new CustomReturnDTO();
            response.setStatusCode(500);
            response.setMessage("파일 다운로드 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
