package com.kone.kitms.config;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.JHipsterProperties;

/**
 * KITMS 정적 리소스 웹 설정 클래스
 * 
 * 이 클래스는 KITMS 시스템의 정적 리소스 처리를 위한 웹 설정을 담당합니다:
 * - 정적 리소스 핸들러 등록
 * - 리소스 위치 및 경로 설정
 * - 캐시 제어 설정
 * - 개발/운영 환경별 설정
 * - 이미지, CSS, JS, HTML 파일 처리
 * - 국제화 리소스 처리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Configuration
@Profile({ JHipsterConstants.SPRING_PROFILE_PRODUCTION, JHipsterConstants.SPRING_PROFILE_DEVELOPMENT })
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {

    protected static final String[] RESOURCE_LOCATIONS = new String[] {
        "classpath:/static/",
        "classpath:/static/content/",
        "classpath:/static/i18n/",
        "file:src/main/webapp/",
        // "file:./images/", // 제거됨 - 보안 이미지 API만 허용
    };
    protected static final String[] RESOURCE_PATHS = new String[] {
        "/*.js",
        "/*.css",
        // 이미지 파일 제외 - 보안 이미지 API만 허용
        // "/*.svg", // 제거됨
        // "/*.png", // 제거됨
        "*.ico",
        "/*.html",
        "/*.map",
        "/content/**",
        "/i18n/*",
        "/.well-known/**",
        // "/images/**", // 제거됨 - 보안 이미지 API만 허용
    };

    private final JHipsterProperties jhipsterProperties;

    public StaticResourcesWebConfiguration(JHipsterProperties jHipsterProperties) {
        this.jhipsterProperties = jHipsterProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceHandlerRegistration resourceHandlerRegistration = appendResourceHandler(registry);
        initializeResourceHandler(resourceHandlerRegistration);
    }

    protected ResourceHandlerRegistration appendResourceHandler(ResourceHandlerRegistry registry) {
        return registry.addResourceHandler(RESOURCE_PATHS);
    }

    protected void initializeResourceHandler(ResourceHandlerRegistration resourceHandlerRegistration) {
        resourceHandlerRegistration.addResourceLocations(RESOURCE_LOCATIONS).setCacheControl(getCacheControl());
    }

    protected CacheControl getCacheControl() {
        return CacheControl.maxAge(getJHipsterHttpCacheProperty(), TimeUnit.DAYS).cachePublic();
    }

    private int getJHipsterHttpCacheProperty() {
        return jhipsterProperties.getHttp().getCache().getTimeToLiveInDays();
    }
}
