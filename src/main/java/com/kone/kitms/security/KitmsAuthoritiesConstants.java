package com.kone.kitms.security;

/**
 * KITMS 시스템 권한 코드 상수 클래스
 * 
 * 이 클래스는 KITMS 시스템의 사용자 권한 코드에 사용되는 상수들을 정의합니다:
 * - 관리자 (0): 시스템 전체 관리 권한
 * - AS총괄 (1): AS 업무 총괄 관리 권한
 * - 승인권자 (2): 승인 업무 처리 권한
 * - AS기사 (3): AS 업무 수행 권한
 * - 고객(요청자) (4): 서비스 요청 권한
 * - 콜센터 (5): 콜센터 업무 처리 권한
 * - 뷰어 (6): 조회 전용 권한
 * - 익명 (9): 로그인하지 않은 사용자
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public final class KitmsAuthoritiesConstants {

    public static final String ADMIN = "0"; //관리자
    public static final String ASTOT = "1"; //AS총괄
    public static final String OKADM = "2"; //승인권자
    public static final String ASUSR = "3"; //AS기사
    public static final String REQUSR = "4"; //고객(요청자)
    public static final String CALLUSR = "5"; //콜센터
    public static final String VIEWER = "6"; //뷰어
    public static final String ANONYMOUS = "9"; //Any

    private KitmsAuthoritiesConstants() {}
}
