package com.kone.kitms.config;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module.Feature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * KITMS Jackson JSON 설정 클래스
 * 
 * 이 클래스는 KITMS 시스템의 Jackson JSON 직렬화/역직렬화 설정을 담당합니다:
 * - Java 8 시간 API 지원 (JavaTimeModule)
 * - JDK 8 모듈 지원 (Jdk8Module)
 * - Hibernate 6 모듈 지원 (Hibernate6Module)
 * - 지연 로딩 객체의 식별자 직렬화 설정
 * - JSON 변환 최적화
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Configuration
public class JacksonConfiguration {

    /**
     * Support for Java date and time API.
     * @return the corresponding Jackson module.
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public Jdk8Module jdk8TimeModule() {
        return new Jdk8Module();
    }

    /*
     * Support for Hibernate types in Jackson.
     */
    @Bean
    public Hibernate6Module hibernate6Module() {
        return new Hibernate6Module().configure(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
    }
}
