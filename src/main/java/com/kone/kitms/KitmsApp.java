package com.kone.kitms;

import com.kone.kitms.config.ApplicationProperties;
import com.kone.kitms.config.CRLFLogConverter;
import jakarta.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

/**
 * KITMS(Key IT Management System) 메인 애플리케이션 클래스
 * 
 * 이 클래스는 Spring Boot 애플리케이션의 진입점으로, 다음과 같은 기능을 제공합니다:
 * - Spring Boot 애플리케이션 자동 설정
 * - 스케줄링 기능 활성화
 * - AOP(Aspect-Oriented Programming) 지원
 * - 설정 프로퍼티 관리
 * - 애플리케이션 시작 시 환경 설정 및 검증
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class KitmsApp {

    private static final Logger log = LoggerFactory.getLogger(KitmsApp.class);

    private final Environment env;

    public KitmsApp(Environment env) {
        this.env = env;
    }

    /**
     * KITMS 애플리케이션 초기화
     * 
     * 애플리케이션 시작 시 다음 작업을 수행합니다:
     * - 한국 시간대(Asia/Seoul) 설정
     * - Spring 프로파일 설정 검증 (dev와 prod 프로파일 동시 사용 방지)
     * - 개발과 클라우드 프로파일 동시 사용 방지
     * 
     * <p>
     * Spring 프로파일은 --spring.profiles.active=your-active-profile 인수로 설정할 수 있습니다.
     * <p>
     * JHipster의 프로파일 작동 방식에 대한 자세한 정보는 
     * <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>에서 확인할 수 있습니다.
     */
    @PostConstruct
    public void initApplication() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not " + "run with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }

    /**
     * 애플리케이션 메인 메서드
     * 
     * KITMS 애플리케이션을 시작하는 진입점입니다.
     * Spring Boot 애플리케이션을 초기화하고 실행합니다.
     *
     * @param args 명령행 인수
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(KitmsApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    /**
     * 애플리케이션 시작 정보 로깅
     * 
     * 애플리케이션이 성공적으로 시작된 후 다음 정보를 로그에 출력합니다:
     * - 애플리케이션 이름
     * - 로컬 및 외부 접근 URL
     * - 활성 프로파일 정보
     * 
     * @param env Spring Environment 객체
     */
    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String applicationName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            CRLFLogConverter.CRLF_SAFE_MARKER,
            """

            ----------------------------------------------------------
            \tApplication '{}' is running! Access URLs:
            \tLocal: \t\t{}://localhost:{}{}
            \tExternal: \t{}://{}:{}{}
            \tProfile(s): \t{}
            ----------------------------------------------------------""",
            applicationName,
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }
}
