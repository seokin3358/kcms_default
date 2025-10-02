package com.kone.kitms.service.dto;

import com.kone.kitms.domain.KitmsAttach;
import com.kone.kitms.domain.KitmsNotice;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class KitmsNoticeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long noticeNo;

    private String noticeTitle;

    private String noticeContent;

    private ZonedDateTime createDt;

    private String createUserId;

    private String createUserName;

    private Boolean staticFlag;

    private List<KitmsAttach> attachList;

    public KitmsNoticeDTO(KitmsNotice kitmsNotice, String userName) {
        this.noticeNo = kitmsNotice.getNoticeNo();
        this.noticeTitle = kitmsNotice.getNoticeTitle();
        this.noticeContent = kitmsNotice.getNoticeContent();
        this.createDt = kitmsNotice.getCreateDt();
        this.createUserId = kitmsNotice.getCreateUserId();
        this.staticFlag = kitmsNotice.getStaticFlag();
        this.createUserName = userName;
    }

    @Override
    public String toString() {
        return (
            "KitmsNoticeDTO{" +
            "noticeNo=" +
            noticeNo +
            ", noticeTitle='" +
            noticeTitle +
            '\'' +
            ", noticeContent='" +
            noticeContent +
            '\'' +
            ", createDt=" +
            createDt +
            ", createUserId='" +
            createUserId +
            '\'' +
            ", createUserName='" +
            createUserName +
            '\'' +
            ", staticFlag=" +
            staticFlag +
            ", attachList=" +
            attachList +
            '}'
        );
    }

    public Long getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(Long noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public ZonedDateTime getCreateDt() {
        return createDt;
    }

    public void setCreateDt(ZonedDateTime createDt) {
        this.createDt = createDt;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Boolean getStaticFlag() {
        return staticFlag;
    }

    public void setStaticFlag(Boolean staticFlag) {
        this.staticFlag = staticFlag;
    }

    public List<KitmsAttach> getAttachList() {
        return attachList;
    }

    public void setAttachList(List<KitmsAttach> attachList) {
        this.attachList = attachList;
    }
}
