package com.kone.kitms.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * KITMS MyBatis 설정 클래스
 * 
 * 이 클래스는 KITMS 시스템의 MyBatis 설정을 담당합니다:
 * - MyBatis 매퍼 스캔 설정
 * - SqlSessionFactory 빈 설정
 * - SqlSessionTemplate 빈 설정
 * - 데이터소스 연결 설정
 * - 매퍼 XML 파일 위치 설정
 * - 타입 별칭 패키지 설정
 * - MyBatis 설정 파일 위치 설정
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Configuration
@MapperScan(basePackages = { "com.kone.kitms.mybatis.mapper", "com.kone.kitms.mapper" }, sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisConfig {

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        // mapper.xml 의 resultType 패키지 주소 생략
        sqlSessionFactoryBean.setTypeAliasesPackage("com.kone.kitms.mybatis.vo");
        // mybatis 설정 파일 세팅
        sqlSessionFactoryBean.setConfigLocation(
            new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml")
        );
        // mapper.xml 위치 패키지 주소
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
