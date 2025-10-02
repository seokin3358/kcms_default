package com.kone.kitms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * KITMS 관리자 메뉴 권한 매핑 엔티티 클래스
 * 
 * 이 클래스는 KITMS 시스템의 관리자 메뉴 권한 매핑 정보를 저장하는 엔티티입니다:
 * - 사용자별 메뉴 접근 권한 관리
 * - 메뉴별 권한 부여/거부 설정
 * - 사용자와 메뉴 간의 다대다 관계 매핑
 * - 생성/수정 일시 및 사용자 추적
 * - JPA 엔티티로 데이터베이스 매핑
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "admin_menu_permission", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "menu_no"}))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminMenuPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_no")
    private Long permissionNo;

    @NotNull
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @NotNull
    @Column(name = "menu_no", nullable = false)
    private Long menuNo;

    @Column(name = "is_granted")
    private Boolean isGranted = true;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private KitmsUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_no", insertable = false, updatable = false)
    private AdminMenu menu;

    // Constructors
    public AdminMenuPermission() {}

    public AdminMenuPermission(String userId, Long menuNo, Boolean isGranted) {
        this.userId = userId;
        this.menuNo = menuNo;
        this.isGranted = isGranted;
    }

    // Getters and Setters
    public Long getPermissionNo() {
        return permissionNo;
    }

    public void setPermissionNo(Long permissionNo) {
        this.permissionNo = permissionNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(Long menuNo) {
        this.menuNo = menuNo;
    }

    public Boolean getIsGranted() {
        return isGranted;
    }

    public void setIsGranted(Boolean isGranted) {
        this.isGranted = isGranted;
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

    public KitmsUser getUser() {
        return user;
    }

    public void setUser(KitmsUser user) {
        this.user = user;
    }

    public AdminMenu getMenu() {
        return menu;
    }

    public void setMenu(AdminMenu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminMenuPermission)) {
            return false;
        }
        return permissionNo != null && permissionNo.equals(((AdminMenuPermission) o).permissionNo);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AdminMenuPermission{" +
                "permissionNo=" + permissionNo +
                ", userId='" + userId + '\'' +
                ", menuNo=" + menuNo +
                ", isGranted=" + isGranted +
                '}';
    }
}
