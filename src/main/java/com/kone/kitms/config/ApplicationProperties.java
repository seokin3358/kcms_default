package com.kone.kitms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * KITMS 애플리케이션 설정 프로퍼티 클래스
 * 
 * 이 클래스는 KITMS 시스템의 애플리케이션별 설정 프로퍼티를 관리합니다.
 * application.yml 파일에서 설정된 프로퍼티들을 Java 객체로 바인딩합니다.
 * 
 * <p>
 * 프로퍼티는 {@code application.yml} 파일에서 설정됩니다.
 * 자세한 예시는 {@link tech.jhipster.config.JHipsterProperties}를 참조하세요.
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    // jhipster-needle-application-properties-property
    // jhipster-needle-application-properties-property-getter
    // jhipster-needle-application-properties-property-class
}
