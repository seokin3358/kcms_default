package com.kone.kitms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * KITMS 첨부파일 엔티티 클래스
 * 
 * 이 클래스는 KITMS 시스템의 첨부파일 정보를 저장하는 엔티티입니다:
 * - 다양한 테이블에 대한 첨부파일 관리
 * - 파일 경로, 이름, 크기 정보 저장
 * - 썸네일 이미지 설정 지원
 * - 파일 바이너리 데이터 저장
 * - 생성 일시 및 사용자 추적
 * - JPA 엔티티로 데이터베이스 매핑
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "kitms_attach")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KitmsAttach implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attach_no")
    private Long attachNo;

    @NotNull
    @Size(max = 30)
    @Column(name = "attach_table_name", length = 30, nullable = false)
    private String attachTableName;

    @NotNull
    @Column(name = "attach_table_pk", nullable = false)
    private Long attachTablePk;

    @NotNull
    @Size(max = 300)
    @Column(name = "attach_file_path", length = 300, nullable = false)
    private String attachFilePath;

    @NotNull
    @Size(max = 100)
    @Column(name = "attach_file_name", length = 100, nullable = false)
    private String attachFileName;

    @Column(name = "attach_file_size", nullable = true)
    private long attachFileSize;

    @NotNull
    @Column(name = "create_dt", nullable = false)
    private ZonedDateTime createDt;

    @NotNull
    @Size(max = 30)
    @Column(name = "create_user_id", length = 30, nullable = false)
    private String createUserId;

    @Lob
    @Column(name = "attach_file", nullable = true)
    private byte[] attachFile;

    @Column(name = "is_thumbnail", nullable = false)
    private Boolean isThumbnail = false;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public byte[] getAttachFile() {
        return this.attachFile;
    }

    public KitmsAttach attachFile(byte[] attachFile) {
        this.setAttachFile(attachFile);
        return this;
    }

    public void setAttachFile(byte[] attachFile) {
        this.attachFile = attachFile;
    }

    public Long getAttachNo() {
        return this.attachNo;
    }

    public KitmsAttach attachNo(Long attachNo) {
        this.setAttachNo(attachNo);
        return this;
    }

    public void setAttachNo(Long attachNo) {
        this.attachNo = attachNo;
    }

    public String getAttachTableName() {
        return this.attachTableName;
    }

    public KitmsAttach attachTableName(String attachTableName) {
        this.setAttachTableName(attachTableName);
        return this;
    }

    public void setAttachTableName(String attachTableName) {
        this.attachTableName = attachTableName;
    }

    public Long getAttachTablePk() {
        return this.attachTablePk;
    }

    public KitmsAttach attachTablePk(Long attachTablePk) {
        this.setAttachTablePk(attachTablePk);
        return this;
    }

    public void setAttachTablePk(Long attachTablePk) {
        this.attachTablePk = attachTablePk;
    }

    public String getAttachFilePath() {
        return this.attachFilePath;
    }

    public KitmsAttach attachFilePath(String attachFilePath) {
        this.setAttachFilePath(attachFilePath);
        return this;
    }

    public long getAttachFileSize() {
        return attachFileSize;
    }

    public void setAttachFileSize(long attachFileSize) {
        this.attachFileSize = attachFileSize;
    }

    public KitmsAttach attachFileSize(long attachFileSize) {
        this.setAttachFileSize(attachFileSize);
        return this;
    }

    public void setAttachFilePath(String attachFilePath) {
        this.attachFilePath = attachFilePath;
    }

    public String getAttachFileName() {
        return this.attachFileName;
    }

    public KitmsAttach attachFileName(String attachFileName) {
        this.setAttachFileName(attachFileName);
        return this;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public ZonedDateTime getCreateDt() {
        return this.createDt;
    }

    public KitmsAttach createDt(ZonedDateTime createDt) {
        this.setCreateDt(createDt);
        return this;
    }

    public void setCreateDt(ZonedDateTime createDt) {
        this.createDt = createDt;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public KitmsAttach createUserId(String createUserId) {
        this.setCreateUserId(createUserId);
        return this;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Boolean getIsThumbnail() {
        return this.isThumbnail;
    }

    public KitmsAttach isThumbnail(Boolean isThumbnail) {
        this.setIsThumbnail(isThumbnail);
        return this;
    }

    public void setIsThumbnail(Boolean isThumbnail) {
        this.isThumbnail = isThumbnail;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KitmsAttach)) {
            return false;
        }
        return getAttachNo() != null && getAttachNo().equals(((KitmsAttach) o).getAttachNo());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "KitmsAttach{" +
            "attachNo=" +
            getAttachNo() +
            ", attachTableName='" +
            getAttachTableName() +
            "'" +
            ", attachTablePk=" +
            getAttachTablePk() +
            ", attachFilePath='" +
            getAttachFilePath() +
            "'" +
            ", attachFileName='" +
            getAttachFileName() +
            "'" +
            ", createDt='" +
            getCreateDt() +
            "'" +
            ", createUserId='" +
            getCreateUserId() +
            "'" +
            "}"
        );
    }
}
