package com.kone.kitms.config;

import static tech.jhipster.config.logging.LoggingUtils.*;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

/**
 * KITMS 로깅 설정 클래스
 * 
 * 이 클래스는 KITMS 시스템의 로깅 설정을 담당합니다:
 * - 콘솔 및 Logstash 로그 어펜더 구성
 * - JSON 형식 로깅 지원
 * - Logstash TCP 소켓 어펜더 설정
 * - 애플리케이션 이름 및 포트 정보 포함
 * - 로그 컨텍스트 리스너 설정
 * - JHipster 로깅 속성 활용
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Configuration
public class LoggingConfiguration {

    public LoggingConfiguration(
        @Value("${spring.application.name}") String appName,
        @Value("${server.port}") String serverPort,
        JHipsterProperties jHipsterProperties,
        ObjectMapper mapper
    ) throws JsonProcessingException {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        Map<String, String> map = new HashMap<>();
        map.put("app_name", appName);
        map.put("app_port", serverPort);
        String customFields = mapper.writeValueAsString(map);

        JHipsterProperties.Logging loggingProperties = jHipsterProperties.getLogging();
        JHipsterProperties.Logging.Logstash logstashProperties = loggingProperties.getLogstash();

        if (loggingProperties.isUseJsonFormat()) {
            addJsonConsoleAppender(context, customFields);
        }
        if (logstashProperties.isEnabled()) {
            addLogstashTcpSocketAppender(context, customFields, logstashProperties);
        }
        if (loggingProperties.isUseJsonFormat() || logstashProperties.isEnabled()) {
            addContextListener(context, customFields, loggingProperties);
        }
    }
}
