package com.kone.kitms.config;

import static org.springframework.security.config.Customizer.withDefaults;

// JWT 기능을 일시적으로 비활성화 (설정 누락으로 인한 오류 방지)
// import com.kone.kitms.security.JwtAuthenticationFilter;
import com.kone.kitms.web.filter.SpaWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import tech.jhipster.config.JHipsterProperties;

/**
 * KITMS 보안 설정 구성 클래스
 * 
 * 이 클래스는 KITMS 시스템의 보안 관련 설정을 담당합니다:
 * - Spring Security 필터 체인 구성
 * - 인증 및 인가 설정
 * - CORS 및 CSRF 설정
 * - 세션 관리 설정
 * - 정적 리소스 접근 권한 설정
 * - 관리자 페이지 보안 설정
 * - 로그인/로그아웃 처리 설정
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final JHipsterProperties jHipsterProperties;
    private final UserDetailsService userDetailsService;
    
    // JWT 기능을 일시적으로 비활성화 (설정 누락으로 인한 오류 방지)
    // @Autowired
    // private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties, UserDetailsService userDetailsService) {
        this.jHipsterProperties = jHipsterProperties;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults())
            .csrf(csrf -> csrf
                // API는 CSRF 예외 처리 (JWT 토큰으로 인증)
                .ignoringRequestMatchers(mvc.pattern("/api/**"))
                // 웹 폼은 CSRF 보호
                .csrfTokenRepository(csrfTokenRepository())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            )
            .authenticationProvider(authenticationProvider())
            // JWT 기능을 일시적으로 비활성화 (설정 누락으로 인한 오류 방지)
            // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
            .headers(headers ->
                headers
                    .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
                    .frameOptions(FrameOptionsConfig::sameOrigin)
                    .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    .permissionsPolicy(permissions ->
                        permissions.policy(
                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"
                        )
                    )
            )
            .authorizeHttpRequests(authz ->
                // prettier-ignore
                authz
                    // 정적 리소스 허용 (이미지 제외)
                    .requestMatchers(mvc.pattern("/static/**")).permitAll()
                    // 이미지 파일 직접 접근 차단 - 보안 이미지 API만 허용
                    // .requestMatchers(mvc.pattern("/images/**")).permitAll() // 제거됨
                    .requestMatchers(mvc.pattern("/js/**")).permitAll()
                    .requestMatchers(mvc.pattern("/style/**")).permitAll()
                    .requestMatchers(mvc.pattern("/fonts/**")).permitAll()
                    .requestMatchers(mvc.pattern("/*.html")).permitAll()
                    .requestMatchers(mvc.pattern("/*.js")).permitAll()
                    .requestMatchers(mvc.pattern("/*.css")).permitAll()
                    .requestMatchers(mvc.pattern("/*.ico")).permitAll()
                    // 이미지 파일 직접 접근 차단
                    // .requestMatchers(mvc.pattern("/*.png")).permitAll() // 제거됨
                    // .requestMatchers(mvc.pattern("/*.svg")).permitAll() // 제거됨
                    .requestMatchers(mvc.pattern("/*.json")).permitAll()
                    .requestMatchers(mvc.pattern("/*.map")).permitAll()
                    .requestMatchers(mvc.pattern("/*.txt")).permitAll()
                    .requestMatchers(mvc.pattern("/*.webapp")).permitAll()
                    
                           // 공개 API만 허용 (새로 추가)
                           .requestMatchers(mvc.pattern("/api/kitms-login")).permitAll()           // 로그인
                           .requestMatchers(mvc.pattern("/api/kitms-login/id")).permitAll()        // ID 찾기
                           .requestMatchers(mvc.pattern("/api/kitms-login/pwd")).permitAll()       // 비밀번호 재설정
                           .requestMatchers(mvc.pattern("/api/csrf-token")).permitAll()            // CSRF 토큰 발급
                           .requestMatchers(mvc.pattern("/api/cms/content/**")).permitAll()        // CMS 컨텐츠 조회
                    .requestMatchers(mvc.pattern("/api/kitms-notices/public/**")).permitAll() // 공개 공지사항
                    .requestMatchers(mvc.pattern("/api/kitms-notices/static")).permitAll()  // 정적 공지사항
                    .requestMatchers(mvc.pattern("/api/kitms-notices/{noticeNo}")).permitAll() // 공지사항 상세 조회
                    .requestMatchers(mvc.pattern("/api/notice/{noticeNo}")).permitAll()        // 공지사항 상세 조회 (별칭)
                    .requestMatchers(mvc.pattern("/api/kitms-attaches/notice/**")).permitAll() // 공지사항 첨부파일
                    .requestMatchers(mvc.pattern("/api/kitms-attaches/list/**")).permitAll()   // 첨부파일 목록 조회
                    .requestMatchers(mvc.pattern("/api/kitms-menus/tree")).permitAll()      // 메뉴 트리 조회
                    .requestMatchers(mvc.pattern("/api/kitms-menus/root")).permitAll()      // 루트 메뉴 조회
                    .requestMatchers(mvc.pattern("/api/kitms-menus/children/**")).permitAll() // 자식 메뉴 조회
                    .requestMatchers(mvc.pattern("/api/menu/tree")).permitAll()             // 메뉴 트리 조회 (별칭)
                    .requestMatchers(mvc.pattern("/api/menu/root")).permitAll()             // 루트 메뉴 조회 (별칭)
                    .requestMatchers(mvc.pattern("/api/menu/children/**")).permitAll()      // 자식 메뉴 조회 (별칭)
                    .requestMatchers(mvc.pattern("/api/header-section/**")).permitAll()     // 헤더 섹션
                    .requestMatchers(mvc.pattern("/api/footer-section/**")).permitAll()     // 푸터 섹션
                    .requestMatchers(mvc.pattern("/api/notice-section/**")).permitAll()     // 공지사항 섹션
                    .requestMatchers(mvc.pattern("/api/secure-images/**")).permitAll()      // 보안 이미지 (일반 사용자용)
                    .requestMatchers(mvc.pattern("/api/newsroom/list")).permitAll()         // 뉴스룸 목록 조회 (프론트엔드용)
                    .requestMatchers(mvc.pattern("/api/newsrooms/public")).permitAll()      // 뉴스룸 공개 목록 조회
                    .requestMatchers(mvc.pattern("/api/newsrooms/recent")).permitAll()      // 최근 뉴스룸 조회
                    .requestMatchers(mvc.pattern("/api/newsrooms/{id}")).permitAll()        // 뉴스룸 상세 조회
                    
                    // 나머지 모든 API는 인증 필요 (핵심 변경)
                    .requestMatchers(mvc.pattern("/api/**")).authenticated()
                    
                    // 관리 API는 관리자 권한 필요
                    .requestMatchers(mvc.pattern("/management/**")).hasRole("ADMIN")
                    
                    // 관리자 로그인 페이지는 허용
                    .requestMatchers(mvc.pattern("/admin/login")).permitAll()
                    .requestMatchers(mvc.pattern("/admin/login.html")).permitAll()
                    
                    // 관리자 페이지는 인증 필요
                    .requestMatchers(mvc.pattern("/admin/**")).authenticated()
                    
                    // 나머지 모든 요청 허용
                    .requestMatchers(mvc.pattern("/**")).permitAll()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .formLogin(form -> form
                .loginPage("/admin/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .failureUrl("/admin/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(exceptions ->
                exceptions
                    .authenticationEntryPoint((request, response, authException) -> {
                        if (request.getRequestURI().startsWith("/admin/")) {
                            response.sendRedirect("/admin/login");
                        } else {
                            response.sendError(401, "Unauthorized");
                        }
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        if (request.getRequestURI().startsWith("/admin/")) {
                            response.sendRedirect("/admin/login?error=access_denied");
                        } else {
                            response.sendError(403, "Access Denied");
                        }
                    })
            );
        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    /**
     * CSRF 토큰 리포지토리 Bean
     * 쿠키 기반 CSRF 토큰 저장소를 설정합니다.
     *
     * @return CsrfTokenRepository CSRF 토큰 리포지토리
     */
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookiePath("/");
        repository.setHeaderName("X-XSRF-TOKEN");
        repository.setCookieName("XSRF-TOKEN");
        return repository;
    }
}
