package com.kone.kitms.mybatis.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.ibatis.type.Alias;
import org.springframework.beans.factory.annotation.Value;

/**
 * KITMS 공통 파라미터 VO 클래스
 * 
 * 이 클래스는 KITMS 시스템의 MyBatis 쿼리에서 사용되는 공통 파라미터를 담는 VO입니다:
 * - 페이징 처리 (start, end, page, size, totalCount)
 * - 검색 조건 (searchColumn, searchValue, searchArray)
 * - 정렬 조건 (sortCol, order)
 * - 조직 정보 (organNo, organNoList)
 * - 사용자 정보 (userId, authCode)
 * - 날짜 범위 검색 (searchStartDt, searchEndDt)
 * - 동적 필드 처리 (dynamicFieldNameList)
 * - 다양한 비즈니스 파라미터들
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Alias("KitmsCommonParamVO")
public class KitmsCommonParamVO {

    int start = 1;
    int end = 100;
    int page = 1;
    int size = 100;
    int totalCount;

    int noticeStaticCount;

    String sortCol;
    String order;

    String[] searchArray;
    String organNo;
    List<String> organNoList;

    String[] searchValueArray;

    String searchColumn;
    String searchValue;
    String compNo;
    String compName;
    String branchNo;
    String branchName;
    String authCode;
    String contNo;
    String assetNo;
    String asUserId;
    String mappingYn;
    String inContNo;
    String serviceNo;
    String userId;
    String state;
    String reqUserId;
    String statusCode;
    String reqUserService;
    String postCode;
    List<Long> compNoList;
    String searchStartDt;
    String searchEndDt;
    String searchDt;
    String stockNo;

    String yyyymm;

    String searchType;

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getStockNo() {
        return stockNo;
    }

    public void setStockNo(String stockNo) {
        this.stockNo = stockNo;
    }

    public String getYyyymm() {
        return yyyymm;
    }

    public void setYyyymm(String yyyymm) {
        this.yyyymm = yyyymm;
    }

    public String getSearchDt() {
        return searchDt;
    }

    public void setSearchDt(String searchDt) {
        this.searchDt = searchDt;
    }

    public String getSearchStartDt() {
        return searchStartDt;
    }

    public void setSearchStartDt(String searchStartDt) {
        this.searchStartDt = searchStartDt;
    }

    public String getSearchEndDt() {
        return searchEndDt;
    }

    public void setSearchEndDt(String searchEndDt) {
        this.searchEndDt = searchEndDt;
    }

    List<String> dynamicFieldNameList = new ArrayList<String>();

    public String[] getSearchValueArray() {
        return searchValueArray;
    }

    public void setSearchValueArray(String[] searchValueArray) {
        this.searchValueArray = searchValueArray;
    }

    public String getOrganNo() {
        return organNo;
    }

    public void setOrganNo(String organNo) {
        this.organNo = organNo;
    }

    public List<String> getOrganNoList() {
        return organNoList;
    }

    public void setOrganNoList(List<String> organNoList) {
        this.organNoList = organNoList;
    }

    public String[] getSearchArray() {
        return searchArray;
    }

    public void setSearchArray(String[] searchArray) {
        this.searchArray = searchArray;
    }

    public List<String> getDynamicFieldNameList() {
        return dynamicFieldNameList;
    }

    public void setDynamicFieldNameList(String fieldPrefix, int size) {
        if (size != 0) {
            for (int i = 1; i <= size; i++) {
                dynamicFieldNameList.add(fieldPrefix.toUpperCase() + i);
            }
        }
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public int getNoticeStaticCount() {
        return noticeStaticCount;
    }

    public void setNoticeStaticCount(int noticeStaticCount) {
        this.noticeStaticCount = noticeStaticCount;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSortCol() {
        return sortCol;
    }

    public void setSortCol(String sortCol) {
        this.sortCol = sortCol;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        //        if (size > 100) size = 100;
        this.size = size;
        this.start = (this.page - 1) * this.size + 1;
        this.end = this.page * this.size;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(String searchColumn) {
        this.searchColumn = searchColumn;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getAsUserId() {
        return asUserId;
    }

    public void setAsUserId(String asUserId) {
        this.asUserId = asUserId;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getCompNo() {
        return compNo;
    }

    public void setCompNo(String compNo) {
        this.compNo = compNo;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getContNo() {
        return contNo;
    }

    public void setContNo(String contNo) {
        this.contNo = contNo;
    }

    public String getMappingYn() {
        return mappingYn;
    }

    public void setMappingYn(String mappingYn) {
        this.mappingYn = mappingYn;
    }

    public String getInContNo() {
        return inContNo;
    }

    public void setInContNo(String inContNo) {
        this.inContNo = inContNo;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReqUserId() {
        return reqUserId;
    }

    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }

    public String getReqUserService() {
        return reqUserService;
    }

    public void setReqUserService(String reqUserService) {
        this.reqUserService = reqUserService;
    }

    public List<Long> getCompNoList() {
        return compNoList;
    }

    public void setCompNoList(List<Long> compNoList) {
        this.compNoList = compNoList;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public String toString() {
        return (
            "KitmsCommonParamVO{" +
            "start=" +
            start +
            ", end=" +
            end +
            ", page=" +
            page +
            ", size=" +
            size +
            ", totalCount=" +
            totalCount +
            ", noticeStaticCount=" +
            noticeStaticCount +
            ", sortCol='" +
            sortCol +
            '\'' +
            ", order='" +
            order +
            '\'' +
            ", searchArray=" +
            Arrays.toString(searchArray) +
            ", organNo='" +
            organNo +
            '\'' +
            ", organNoList=" +
            organNoList +
            ", searchValueArray=" +
            Arrays.toString(searchValueArray) +
            ", searchColumn='" +
            searchColumn +
            '\'' +
            ", searchValue='" +
            searchValue +
            '\'' +
            ", compNo='" +
            compNo +
            '\'' +
            ", compName='" +
            compName +
            '\'' +
            ", branchNo='" +
            branchNo +
            '\'' +
            ", branchName='" +
            branchName +
            '\'' +
            ", authCode='" +
            authCode +
            '\'' +
            ", contNo='" +
            contNo +
            '\'' +
            ", assetNo='" +
            assetNo +
            '\'' +
            ", asUserId='" +
            asUserId +
            '\'' +
            ", mappingYn='" +
            mappingYn +
            '\'' +
            ", inContNo='" +
            inContNo +
            '\'' +
            ", serviceNo='" +
            serviceNo +
            '\'' +
            ", userId='" +
            userId +
            '\'' +
            ", state='" +
            state +
            '\'' +
            ", reqUserId='" +
            reqUserId +
            '\'' +
            ", statusCode='" +
            statusCode +
            '\'' +
            ", reqUserService='" +
            reqUserService +
            '\'' +
            ", postCode='" +
            postCode +
            '\'' +
            ", compNoList=" +
            compNoList +
            ", searchStartDt='" +
            searchStartDt +
            '\'' +
            ", searchEndDt='" +
            searchEndDt +
            '\'' +
            ", searchDt='" +
            searchDt +
            '\'' +
            ", stockNo='" +
            stockNo +
            '\'' +
            ", yyyymm='" +
            yyyymm +
            '\'' +
            ", searchType='" +
            searchType +
            '\'' +
            ", dynamicFieldNameList=" +
            dynamicFieldNameList +
            '}'
        );
    }
}
