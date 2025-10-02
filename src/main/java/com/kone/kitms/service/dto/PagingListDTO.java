package com.kone.kitms.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PagingListDTO {

    private List<?> list;
    private Page page;

    public PagingListDTO(List<?> list, int page, int size, int total) {
        this.list = list;
        this.page = new Page(page, size, total);
    }

    @JsonProperty("list")
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    @JsonProperty("page")
    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

class Page {

    private int currentPage;
    private int size;
    private int total;
    private int totalPages;

    public Page(int page, int size, int total) {
        this.currentPage = page;
        this.size = size;
        this.total = total;
        this.totalPages = (int) Math.ceil((double) total / size);
    }

    @JsonProperty("currentPage")
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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

    @JsonProperty("totalPages")
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
