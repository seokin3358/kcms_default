package com.kone.kitms.aop.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * KITMS 호출 권한 검사 어노테이션
 * 
 * 이 어노테이션은 KITMS 시스템의 메서드 호출 권한을 검사하는 데 사용됩니다:
 * - 클래스 레벨에서 적용 가능
 * - 런타임에 유지되는 어노테이션
 * - 페이지 ID를 통한 권한 검사
 * - AOP를 통한 권한 검증 자동화
 * - 보안 강화 및 접근 제어
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CallPermCheck {
    /**
     * 권한 검사에 사용될 페이지 ID
     * 
     * @return 페이지 ID 문자열
     */
    String pageId();
}
