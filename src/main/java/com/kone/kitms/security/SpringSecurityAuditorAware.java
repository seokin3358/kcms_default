package com.kone.kitms.security;

import com.kone.kitms.config.Constants;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * KITMS Spring Security 기반 감사자 인식 클래스
 * 
 * 이 클래스는 KITMS 시스템의 JPA 감사(Auditing) 기능을 위한 감사자 정보를 제공합니다:
 * - Spring Security 컨텍스트에서 현재 사용자 정보 조회
 * - JPA 엔티티의 생성/수정 시 사용자 정보 자동 설정
 * - 로그인하지 않은 경우 시스템 사용자로 설정
 * - 데이터 변경 이력 추적을 위한 감사자 정보 제공
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM));
    }
}
