-- 관리자 메뉴 권한 매핑 테이블 생성
CREATE TABLE admin_menu_permission (
    permission_no BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '권한 번호',
    user_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
    menu_no BIGINT NOT NULL COMMENT '메뉴 번호',
    is_granted BOOLEAN DEFAULT TRUE COMMENT '권한 부여 여부',
    created_by VARCHAR(50) COMMENT '생성자',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    UNIQUE KEY uk_user_menu (user_id, menu_no),
    INDEX idx_user_id (user_id),
    INDEX idx_menu_no (menu_no),
    INDEX idx_is_granted (is_granted),
    
    FOREIGN KEY (user_id) REFERENCES kitms_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (menu_no) REFERENCES admin_menu(menu_no) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='관리자 메뉴 권한 매핑 테이블';

-- 메인 관리자(001)에게는 모든 메뉴 권한 부여
INSERT INTO admin_menu_permission (user_id, menu_no, is_granted, created_by, created_at)
SELECT 'admin', menu_no, TRUE, 'admin', NOW()
FROM admin_menu
WHERE is_active = TRUE;

-- 기존 서브 관리자들에게는 기본 메뉴만 권한 부여 (대시보드, 콘텐츠 관리, 사용자 관리)
INSERT INTO admin_menu_permission (user_id, menu_no, is_granted, created_by, created_at)
SELECT u.user_id, m.menu_no, TRUE, 'admin', NOW()
FROM kitms_user u
CROSS JOIN admin_menu m
WHERE u.auth_code = '002' -- 서브 관리자
  AND m.menu_name IN ('대시보드', '콘텐츠 관리', '공지사항 관리', '보도자료 관리', '메뉴 관리')
  AND m.is_active = TRUE;
