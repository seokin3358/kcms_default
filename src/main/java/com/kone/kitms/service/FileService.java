package com.kone.kitms.service;

import com.kone.kitms.domain.KitmsAttach;
import com.kone.kitms.repository.KitmsAttachRepository;
import com.kone.kitms.service.dto.CustomReturnDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

/**
 * KITMS 파일 관리 서비스 클래스
 * 
 * 이 클래스는 KITMS 시스템의 파일 관리를 위한 비즈니스 로직을 제공합니다:
 * - 첨부파일 업로드 및 다운로드
 * - 파일 압축 및 ZIP 다운로드
 * - 파일 삭제 및 관리
 * - BLOB 데이터 처리
 * - 파일 경로 및 메타데이터 관리
 * - 다중 파일 처리
 * - 파일 중복 방지 및 고유명 생성
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Service
public class FileService {

    @Value("${attach.file.base-path}")
    private String fileBasePath;

    private static final String ENTITY_NAME = "kitmsAttach";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KitmsLoginService kitmsLoginService;

    private final KitmsAttachRepository kitmsAttachRepository;

    public FileService(KitmsLoginService kitmsLoginService, KitmsAttachRepository kitmsAttachRepository) {
        this.kitmsLoginService = kitmsLoginService;
        this.kitmsAttachRepository = kitmsAttachRepository;
    }

    public void getKitmsFileDownload(HttpServletResponse response, Long attachNo) throws IOException {
        Optional<KitmsAttach> byId = kitmsAttachRepository.findById(attachNo);
        KitmsAttach attach = byId.orElseThrow();

        String path = attach.getAttachFilePath();
        String fileName = attach.getAttachFileName();
        byte[] blob = attach.getAttachFile();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        if (blob == null) {
            Path filePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
            blob = resource.getContentAsByteArray();
        }
        response.getOutputStream().write(blob);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    public void getKitmsFileDownloadZip(HttpServletResponse response, String tableName, Long tablePK) throws IOException {
        List<KitmsAttach> attachList = kitmsAttachRepository.findAllByAttachTableNameAndAttachTablePk(tableName, tablePK);

        List<File> fileList = new ArrayList<File>();
        if (attachList != null && attachList.size() > 0) {
            for (KitmsAttach attach : attachList) {
                fileList.add(new File(attach.getAttachFilePath()));
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + tableName + "_" + tablePK + ".zip\"");

            ZipOutputStream zipOut = null;
            FileInputStream fis = null;
            zipOut = new ZipOutputStream(response.getOutputStream());

            for (File file : fileList) {
                zipOut.putNextEntry(new ZipEntry(file.getName()));
                fis = new FileInputStream(file);
                StreamUtils.copy(fis, zipOut);
                fis.close();
                zipOut.closeEntry();
            }
            zipOut.close();
        }
    }

    public void removeFile(String tableName, Long tablePK) {
        List<KitmsAttach> attachList = kitmsAttachRepository.findAllByAttachTableNameAndAttachTablePk(tableName, tablePK);
        if (attachList != null && attachList.size() > 0) {
            for (KitmsAttach ka : attachList) {
                File attachFile = new File(ka.getAttachFilePath());
                if (attachFile.exists()) attachFile.delete();
            }
        }
        kitmsAttachRepository.deleteByAttachTableNameAndAttachTablePk(tableName, tablePK);
    }

    public CustomReturnDTO removeFileSingle(String tableName, Long attachNo) {
        KitmsAttach attach = kitmsAttachRepository.findByAttachTableNameAndAttachNo(tableName, attachNo);
        CustomReturnDTO result = new CustomReturnDTO();
        result.setStatus(HttpStatus.OK);
        if (attach != null) {
            kitmsAttachRepository.deleteByAttachTableNameAndAttachNo(tableName, attachNo);
        } else {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    public void createFile(
        HttpServletRequest request,
        String tableName,
        Long tablePK,
        List<MultipartFile> multipartFileList,
        Boolean saveBlob
    ) throws IOException {
        removeFile(tableName, tablePK);

        if (multipartFileList != null && multipartFileList.size() > 0) {
            for (MultipartFile m : multipartFileList) {
                KitmsAttach kitmsAttach = makeAttachData(tableName, tablePK, m);
                if (kitmsAttach != null) {
                    kitmsAttach.setCreateUserId(kitmsLoginService.getTokenUserId(request));
                    if (saveBlob) {
                        kitmsAttach.setAttachFile(m.getBytes());
                    } else {
                        m.transferTo(new File(kitmsAttach.getAttachFilePath()));
                    }
                    kitmsAttachRepository.save(kitmsAttach);
                }
            }
        }
    }

    public Long createFile(HttpServletRequest request, MultipartFile multipartFile, String tableName, Long tablePk, Boolean saveBlob)
        throws IOException {
        removeFile(tableName, tablePk);

        KitmsAttach kitmsAttach = makeAttachData(tableName, tablePk, multipartFile);
        if (kitmsAttach != null) {
            kitmsAttach.setCreateUserId(kitmsLoginService.getTokenUserId(request));
            if (saveBlob) {
                kitmsAttach.setAttachFile(multipartFile.getBytes());
            } else {
                multipartFile.transferTo(new File(kitmsAttach.getAttachFilePath()));
            }
            return kitmsAttachRepository.save(kitmsAttach).getAttachNo();
        }
        return null;
    }

    public void updateFile(HttpServletRequest request, MultipartFile multipartFile, Long fileNo) throws IOException {
        String fileOriginName = multipartFile.getOriginalFilename();
        String filePath = fileBasePath + fileOriginName;

        kitmsAttachRepository
            .findById(fileNo)
            .map(attachFile -> {
                attachFile.setAttachFileName(fileOriginName);
                attachFile.setAttachFilePath(filePath);
                attachFile.setCreateDt(ZonedDateTime.now().plusHours(9));
                attachFile.setCreateUserId(kitmsLoginService.getTokenUserId(request));

                try {
                    attachFile.setAttachFile(multipartFile.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return attachFile;
            })
            .map(kitmsAttachRepository::save);
    }

    public Blob getBlobData(MultipartFile multipartFile) throws IOException, SQLException {
        byte[] bytes = multipartFile.getBytes();
        Blob blob = new SerialBlob(bytes);
        return blob;
    }

    public void updateNewAndDeleteFile(
        HttpServletRequest request,
        String tableName,
        Long tablePK,
        List<MultipartFile> multipartFileList,
        List<Long> deleteAttachNoList
    ) throws IOException {
        if (deleteAttachNoList != null && deleteAttachNoList.size() > 0) {
            for (Long fileNo : deleteAttachNoList) {
                Optional<KitmsAttach> byId = kitmsAttachRepository.findById(fileNo);
                if (byId != null) {
                    KitmsAttach attach = byId.orElseThrow();
                    File attachFile = new File(attach.getAttachFilePath());
                    if (attachFile.exists()) attachFile.delete();
                    kitmsAttachRepository.deleteById(fileNo);
                }
            }
        }

        if (multipartFileList != null && multipartFileList.size() > 0) {
            for (MultipartFile m : multipartFileList) {
                KitmsAttach kitmsAttach = makeAttachData(tableName, tablePK, m);
                if (kitmsAttach != null) {
                    kitmsAttach.setCreateUserId(kitmsLoginService.getTokenUserId(request));
                    m.transferTo(new File(kitmsAttach.getAttachFilePath()));
                    kitmsAttachRepository.save(kitmsAttach);
                }
            }
        }
    }

    public KitmsAttach makeAttachData(String tableName, Long tablePK, MultipartFile mFile) {
        KitmsAttach kitmsAttach = new KitmsAttach();

        String fileOriginName = mFile.getOriginalFilename();

        if (fileOriginName == null || fileOriginName.equals("") || fileOriginName.isBlank() || fileOriginName.isEmpty()) return null;

        // 물리저장소 파일 중복방지를 위한 table명 + pk
        String filePath = fileBasePath + tableName + "_" + tablePK + "_" + fileOriginName;

        kitmsAttach.setAttachFileName(fileOriginName);
        kitmsAttach.setAttachFilePath(filePath);
        kitmsAttach.setAttachTableName(tableName);
        kitmsAttach.setCreateDt(ZonedDateTime.now().plusHours(9));
        kitmsAttach.setAttachTablePk(tablePK);
        kitmsAttach.setAttachFileSize(mFile.getSize());

        return kitmsAttach;
    }

    public void deleteFile(Long fileNo) {
        kitmsAttachRepository.deleteById(fileNo);
    }
}
