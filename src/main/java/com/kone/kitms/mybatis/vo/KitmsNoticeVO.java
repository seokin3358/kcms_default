package com.kone.kitms.mybatis.vo;

import java.time.ZonedDateTime;
import org.apache.ibatis.type.Alias;

/**
 * KITMS 공지사항 VO 클래스
 * 
 * 이 클래스는 KITMS 시스템의 공지사항 정보를 담는 VO입니다:
 * - 공지사항 기본 정보 (noticeNo, noticeTitle, noticeContent)
 * - 공지사항 상태 정보 (staticFlag: 고정 공지 여부)
 * - 작성자 정보 (createUserId, createUserName)
 * - 생성 일시 (createDt)
 * - 첨부파일 존재 여부 (fileExist)
 * - 총 개수 정보 (totalCount)
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Alias("KitmsNoticeVO")
public class KitmsNoticeVO {

    String noticeNo;
    String noticeTitle;
    String noticeContent;
    String staticFlag;
    String createUserId;
    String createUserName;
    String createDt;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    String totalCount;
    String fileExist;

    public String getFileExist() {
        return fileExist;
    }

    public void setFileExist(String fileExist) {
        this.fileExist = fileExist;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
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

    public String getStaticFlag() {
        return staticFlag;
    }

    public void setStaticFlag(String staticFlag) {
        this.staticFlag = staticFlag;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }
}
