package com.kone.kitms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * KITMS 날짜/시간 형식 설정 클래스
 * 
 * 이 클래스는 KITMS 시스템의 날짜/시간 형식 변환기를 설정합니다:
 * - 기본적으로 ISO 형식을 사용하도록 설정
 * - 날짜/시간 변환기 등록
 * - 웹 MVC 설정 구성
 * - 표준화된 날짜/시간 형식 지원
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);
    }
}
