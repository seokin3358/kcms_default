package com.kone.kitms.aop.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * KITMS 토큰 검사 제외 어노테이션
 * 
 * 이 어노테이션은 KITMS 시스템의 특정 메서드에서 토큰 검사를 제외하는 데 사용됩니다:
 * - 메서드 레벨에서 적용 가능
 * - 런타임에 유지되는 어노테이션
 * - TokenAspect의 토큰 검사에서 제외
 * - 특정 메서드에 대한 토큰 검사 우회
 * - 공개 API나 인증이 필요하지 않은 메서드 처리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExTokenCheck {
}
