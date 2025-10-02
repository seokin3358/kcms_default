package com.kone.kitms;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import tech.jhipster.config.DefaultProfileUtil;

/**
 * KITMS 웹 애플리케이션 XML 설정 대체 클래스
 * 
 * 이 클래스는 KITMS 애플리케이션의 웹 배포를 위한 헬퍼 클래스입니다:
 * - web.xml 파일 생성의 대안으로 사용
 * - Tomcat, JBoss 등의 서블릿 컨테이너에 배포 시에만 호출됨
 * - Spring Boot 서블릿 초기화 지원
 * - 기본 프로파일 설정 및 애플리케이션 소스 설정
 * - WAR 파일 배포 시 필요한 설정 제공
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public class ApplicationWebXml extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // set a default to use when no profile is configured.
        DefaultProfileUtil.addDefaultProfile(application.application());
        return application.sources(KitmsApp.class);
    }
}
