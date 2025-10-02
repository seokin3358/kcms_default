package com.kone.kitms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * KITMS 관리자 메뉴 엔티티 클래스
 * 
 * 이 클래스는 KITMS 시스템의 관리자 메뉴 정보를 저장하는 엔티티입니다:
 * - 메뉴 이름, URL, 아이콘 정보 관리
 * - 메뉴 순서 및 활성화 상태 관리
 * - 메뉴 가시성 설정
 * - 생성/수정 일시 및 사용자 추적
 * - JPA 엔티티로 데이터베이스 매핑
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "admin_menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_no")
    private Long menuNo;

    @NotNull
    @Size(max = 100)
    @Column(name = "menu_name", length = 100, nullable = false)
    private String menuName;

    @Size(max = 200)
    @Column(name = "menu_url", length = 200)
    private String menuUrl;

    @Size(max = 50)
    @Column(name = "menu_icon", length = 50)
    private String menuIcon;

    @Column(name = "menu_order")
    private Integer menuOrder = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_visible")
    private Boolean isVisible = true;

    @Size(max = 500)
    @Column(name = "menu_description", length = 500)
    private String menuDescription;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // 계층구조 제거로 children 필드도 제거

    // Constructors
    public AdminMenu() {}

    public AdminMenu(String menuName, String menuUrl) {
        this.menuName = menuName;
        this.menuUrl = menuUrl;
    }

    // Getters and Setters
    public Long getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(Long menuNo) {
        this.menuNo = menuNo;
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

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminMenu)) {
            return false;
        }
        return menuNo != null && menuNo.equals(((AdminMenu) o).menuNo);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AdminMenu{" +
                "menuNo=" + menuNo +
                ", menuName='" + menuName + '\'' +
                ", menuUrl='" + menuUrl + '\'' +
                ", menuOrder=" + menuOrder +
                ", isActive=" + isActive +
                '}';
    }
}
