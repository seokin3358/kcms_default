package com.kone.kitms.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * KITMS QueryDSL 설정 클래스
 * 
 * 이 클래스는 KITMS 시스템의 QueryDSL 설정을 담당합니다:
 * - JPAQueryFactory 빈 설정
 * - EntityManager 주입
 * - 타입 안전한 쿼리 작성 지원
 * - 동적 쿼리 생성 지원
 * - JPA 기반 QueryDSL 설정
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
