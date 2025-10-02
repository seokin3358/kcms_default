package com.kone.kitms.aop.logging;

import com.kone.kitms.service.KitmsLoginService;
import com.kone.kitms.service.dto.CustomReturnDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.jhipster.config.JHipsterConstants;

/**
 * KITMS 토큰 검증 AOP 클래스
 * 
 * 이 클래스는 KITMS 시스템의 REST API 호출에 대한 토큰 검증을 자동으로 수행하는 AOP입니다.
 * @ExTokenCheck 어노테이션이 없는 모든 REST API 메서드에 대해 토큰 유효성을 검증합니다.
 * 
 * 주요 기능:
 * - REST API 호출 시 자동 토큰 검증
 * - 토큰이 유효하지 않은 경우 자동으로 오류 응답 반환
 * - 개발 환경에서만 동작 (dev 프로파일)
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Component
@Aspect
public class TokenAspect {

    private final Environment env;
    private final KitmsLoginService kitmsLoginService;

    public TokenAspect(Environment env, KitmsLoginService kitmsLoginService) {
        this.env = env;
        this.kitmsLoginService = kitmsLoginService;
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    //    @Pointcut("within(com.kone.kitms.web.rest.*) && !@annotation(com.kone.kitms.aop.logging.ExTokenCheck)")
    @Pointcut(
        "within(com.kone.kitms.web.rest.*) " +
        "&& !@annotation(com.kone.kitms.aop.logging.ExTokenCheck)"
    )
    public void applicationRestApiPointcut() {}

    @Around("applicationRestApiPointcut()")
    public Object restApiCallAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // Spring Security JWT 필터가 인증을 처리하므로 AOP 토큰 검증은 비활성화
        // 개발 환경에서만 기존 토큰 검증 로직 유지 (하위 호환성)
        
        if (env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT))) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            Object returnResponse = joinPoint.proceed();

            if (returnResponse instanceof CustomReturnDTO) {
                // 개발 환경에서만 기존 토큰 검증 수행
                CustomReturnDTO returnDto = kitmsLoginService.validToken(request);
                if (returnDto.getStatusCode() != 200) return returnDto;

                return (CustomReturnDTO) returnResponse;
            } else {
                return returnResponse;
            }
        } else {
            // 운영 환경에서는 Spring Security가 인증을 처리하므로 AOP 검증 생략
            return joinPoint.proceed();
        }
    }
}
