package com.kone.kitms.aop.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * KITMS 호출 권한 검사 제외 어노테이션
 * 
 * 이 어노테이션은 KITMS 시스템의 특정 메서드에서 권한 검사를 제외하는 데 사용됩니다:
 * - 메서드 레벨에서 적용 가능
 * - 런타임에 유지되는 어노테이션
 * - CallPermCheck 어노테이션의 권한 검사에서 제외
 * - 특정 메서드에 대한 권한 검사 우회
 * - 보안 예외 처리 및 특수 케이스 처리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExCallPermCheck {
}
