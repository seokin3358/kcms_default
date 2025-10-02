package com.kone.kitms.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * KITMS 메뉴 엔티티
 * 
 * 웹사이트의 네비게이션 메뉴를 트리 구조로 관리하는 엔티티입니다.
 * 계층적 메뉴 구조를 지원하며, 메뉴의 활성화/비활성화 상태를 관리합니다.
 * 
 * 주요 기능:
 * - 계층적 메뉴 구조 지원 (부모-자식 관계)
 * - 메뉴 순서 및 레벨 관리
 * - 메뉴 활성화/비활성화 상태 관리
 * - 새 창 열기 옵션 지원
 * - 메뉴 경로 추적 (breadcrumb용)
 * 
 * 주요 필드:
 * - menuName: 메뉴명
 * - menuUrl: 메뉴 링크 URL
 * - parentId: 부모 메뉴 ID (트리 구조용)
 * - menuLevel: 메뉴 레벨 (1: 최상위, 2: 하위 등)
 * - sortOrder: 메뉴 정렬 순서
 * - isActive: 메뉴 활성화 여부 (Y/N)
 * - isNewWindow: 새 창 열기 여부 (Y/N)
 * 
 * @author KITMS Development Team
 * @version 1.0
 */
public class KitmsMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long menuId;
    private String menuName;
    private String menuUrl;
    private Long parentId;
    private Integer menuLevel;
    private Integer sortOrder;
    private String isActive;
    private String isNewWindow;
    private String menuDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    // 트리 구조를 위한 필드들
    @JsonManagedReference
    private List<KitmsMenu> children = new ArrayList<>();
    
    @JsonBackReference
    private KitmsMenu parent;
    
    private String menuPath; // 메뉴 경로 (예: 케이원소개 > 회사소개)
    
    // 생성자
    public KitmsMenu() {}
    
    public KitmsMenu(String menuName, String menuUrl, Long parentId, Integer menuLevel) {
        this.menuName = menuName;
        this.menuUrl = menuUrl;
        this.parentId = parentId;
        this.menuLevel = menuLevel;
        this.isActive = "Y";
        this.isNewWindow = "N";
        this.sortOrder = 0;
    }

    // Getter and Setter
    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(Integer menuLevel) {
        this.menuLevel = menuLevel;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsNewWindow() {
        return isNewWindow;
    }

    public void setIsNewWindow(String isNewWindow) {
        this.isNewWindow = isNewWindow;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<KitmsMenu> getChildren() {
        return children;
    }

    public void setChildren(List<KitmsMenu> children) {
        this.children = children;
    }

    public KitmsMenu getParent() {
        return parent;
    }

    public void setParent(KitmsMenu parent) {
        this.parent = parent;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    // 유틸리티 메서드
    public boolean isRoot() {
        return parentId == null;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public void addChild(KitmsMenu child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        child.setParent(this);
    }

    @Override
    public String toString() {
        return "KitmsMenu{" +
                "menuId=" + menuId +
                ", menuName='" + menuName + '\'' +
                ", menuUrl='" + menuUrl + '\'' +
                ", parentId=" + parentId +
                ", menuLevel=" + menuLevel +
                ", sortOrder=" + sortOrder +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
