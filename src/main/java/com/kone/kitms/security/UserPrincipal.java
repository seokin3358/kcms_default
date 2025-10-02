package com.kone.kitms.security;

import com.kone.kitms.domain.KitmsUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * KITMS 사용자 인증 정보를 담는 UserDetails 구현 클래스
 * 
 * 이 클래스는 Spring Security의 UserDetails 인터페이스를 구현하여
 * KITMS 시스템의 사용자 인증 정보를 관리합니다:
 * - 사용자 ID, 이메일, 비밀번호 정보 저장
 * - 사용자 권한 정보 관리
 * - 계정 활성화 상태 관리
 * - JWT 토큰 생성 및 검증에 사용
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public class UserPrincipal implements UserDetails {
    
    private Long id;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;

    public UserPrincipal(Long id, String username, String email, String password, 
                        Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.enabled = true;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.accountNonLocked = true;
    }

    /**
     * KitmsUser 엔티티로부터 UserPrincipal 생성
     * 
     * @param user KitmsUser 엔티티
     * @return UserPrincipal 인스턴스
     */
    public static UserPrincipal create(KitmsUser user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_USER")
        );
        
        // 관리자 권한 부여 (authCode가 "0"이면 관리자)
        if ("0".equals(user.getAuthCode())) {
            authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_ADMIN")
            );
        }

        return new UserPrincipal(
            user.getUserNo(),
            user.getUserId(),
            user.getUserEmail(),
            user.getUserPwd(),
            authorities
        );
    }

    /**
     * 사용자 ID로부터 UserPrincipal 생성 (JWT 토큰 검증용)
     * 
     * @param userId 사용자 ID
     * @return UserPrincipal 인스턴스
     */
    public static UserPrincipal create(Long userId) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_USER")
        );
        
        return new UserPrincipal(userId, null, null, null, authorities);
    }

    // Getters
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
}
