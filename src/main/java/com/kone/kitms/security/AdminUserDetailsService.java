package com.kone.kitms.security;

import com.kone.kitms.domain.KitmsUser;
import com.kone.kitms.repository.KitmsUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * KITMS 관리자 인증을 위한 UserDetailsService
 * 
 * 이 클래스는 KITMS 시스템의 관리자 인증을 처리하는 서비스입니다:
 * - 사용자 ID로 관리자 정보 조회
 * - 관리자 권한 부여 (authCode가 "0"이면 관리자)
 * - 사용자 계정 활성화 상태 확인
 * - Spring Security와 연동하여 인증 처리
 * - 로그인 실패 시 예외 처리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Service
public class AdminUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AdminUserDetailsService.class);

    private final KitmsUserRepository kitmsUserRepository;

    public AdminUserDetailsService(KitmsUserRepository kitmsUserRepository) {
        this.kitmsUserRepository = kitmsUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("관리자 인증 시도: {}", username);

        Optional<KitmsUser> userOptional = kitmsUserRepository.findKitmsUserByUserId(username);
        
        if (userOptional.isEmpty()) {
            log.warn("관리자 사용자를 찾을 수 없음: {}", username);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        KitmsUser kitmsUser = userOptional.get();
        
        // 관리자 권한 부여 (authCode가 "0"이면 관리자)
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("0".equals(kitmsUser.getAuthCode())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        log.debug("관리자 인증 성공: {} (권한: {})", username, authorities);

        return new User(
            kitmsUser.getUserId(),
            kitmsUser.getUserPwd(),
            kitmsUser.getEnable() != null ? kitmsUser.getEnable() : true, // enabled
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            authorities
        );
    }
}