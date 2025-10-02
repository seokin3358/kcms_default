package com.kone.kitms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import tech.jhipster.config.locale.AngularCookieLocaleResolver;

/**
 * KITMS 로케일 설정 클래스
 * 
 * 이 클래스는 KITMS 시스템의 다국어 지원을 위한 로케일 설정을 담당합니다:
 * - Angular 쿠키 기반 로케일 리졸버 설정
 * - 언어 변경 인터셉터 구성
 * - 다국어 지원을 위한 웹 MVC 설정
 * - 언어 파라미터 처리
 * - 국제화(i18n) 지원
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        return new AngularCookieLocaleResolver("NG_TRANSLATE_LANG_KEY");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        registry.addInterceptor(localeChangeInterceptor);
    }
}
