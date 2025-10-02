package com.kone.kitms.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class CustomDisplayInfoDTO {

    List<String> headerList;
    List<String> columnList;
    List<List<String>> contentList;
    List<String> keyList;
    List<String> typeList;
    String pkColumnName;

    PageForCustom page;

    public void makePageForCustom(int page, int size, int total) {
        this.page = new PageForCustom(page, size, total);
    }

    public CustomDisplayInfoDTO(
        List<String> headerList,
        List<String> columnList,
        List<List<String>> contentList,
        List<String> keyList,
        List<String> typeList,
        String pkColumnName
    ) {
        this.headerList = headerList;
        this.columnList = columnList;
        this.contentList = contentList;
        this.keyList = keyList;
        this.typeList = typeList;
        this.pkColumnName = pkColumnName;
    }

    public String getPkColumnName() {
        return pkColumnName;
    }

    public void setPkColumnName(String pkColumnName) {
        this.pkColumnName = pkColumnName;
    }

    @JsonProperty("headerList")
    public List<String> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<String> headerList) {
        this.headerList = headerList;
    }

    @JsonProperty("columnList")
    public List<String> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }

    @JsonProperty("contentList")
    public List<List<String>> getContentList() {
        return contentList;
    }

    public void setContentList(List<List<String>> contentList) {
        this.contentList = contentList;
    }

    @JsonProperty("keyList")
    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }

    @JsonProperty("typeList")
    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    @JsonProperty("page")
    public PageForCustom getPage() {
        return page;
    }

    public void setPage(PageForCustom page) {
        this.page = page;
    }
}

class PageForCustom {

    private int page;
    private int size;
    private int total;

    public PageForCustom(int page, int size, int total) {
        this.page = page;
        this.size = size;
        this.total = total;
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
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
