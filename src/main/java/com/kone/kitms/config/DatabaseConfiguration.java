package com.kone.kitms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * KITMS 데이터베이스 설정 클래스
 * 
 * 이 클래스는 KITMS 시스템의 데이터베이스 설정을 담당합니다:
 * - JPA 리포지토리 활성화
 * - JPA 감사(Auditing) 기능 활성화
 * - 트랜잭션 관리 활성화
 * - Spring Security 기반 감사자 설정
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Configuration
@EnableJpaRepositories({ "com.kone.kitms.repository" })
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {}
