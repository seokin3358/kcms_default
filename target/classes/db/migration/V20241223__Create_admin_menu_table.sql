-- 어드민 메뉴 테이블 생성
CREATE TABLE admin_menu (
    menu_no BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '메뉴 번호',
    menu_name VARCHAR(100) NOT NULL COMMENT '메뉴명',
    menu_url VARCHAR(200) COMMENT '메뉴 URL',
    menu_icon VARCHAR(50) COMMENT '메뉴 아이콘',
    parent_menu_no BIGINT COMMENT '부모 메뉴 번호',
    menu_order INT DEFAULT 0 COMMENT '메뉴 순서',
    menu_level INT DEFAULT 1 COMMENT '메뉴 레벨 (1: 최상위, 2: 하위)',
    is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
    is_visible BOOLEAN DEFAULT TRUE COMMENT '표시 여부',
    menu_description VARCHAR(500) COMMENT '메뉴 설명',
    created_by VARCHAR(50) COMMENT '생성자',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    INDEX idx_parent_menu_no (parent_menu_no),
    INDEX idx_menu_level (menu_level),
    INDEX idx_menu_order (menu_order),
    INDEX idx_is_active (is_active),
    INDEX idx_is_visible (is_visible),
    
    FOREIGN KEY (parent_menu_no) REFERENCES admin_menu(menu_no) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='어드민 메뉴 테이블';

-- 샘플 메뉴 데이터 삽입
INSERT INTO admin_menu (
    menu_name,
    menu_url,
    menu_icon,
    parent_menu_no,
    menu_order,
    menu_level,
    is_active,
    is_visible,
    menu_description,
    created_by,
    created_at
) VALUES 
-- 최상위 메뉴들
(
    '대시보드',
    '/admin/dashboard.html',
    'dashboard',
    NULL,
    1,
    1,
    TRUE,
    TRUE,
    '관리자 대시보드',
    'admin',
    NOW()
),
(
    '콘텐츠 관리',
    NULL,
    'content_copy',
    NULL,
    2,
    1,
    TRUE,
    TRUE,
    '콘텐츠 관리 메뉴',
    'admin',
    NOW()
),
(
    '사용자 관리',
    NULL,
    'people',
    NULL,
    3,
    1,
    TRUE,
    TRUE,
    '사용자 관리 메뉴',
    'admin',
    NOW()
),
(
    '시스템 관리',
    NULL,
    'settings',
    NULL,
    4,
    1,
    TRUE,
    TRUE,
    '시스템 관리 메뉴',
    'admin',
    NOW()
);

-- 하위 메뉴들 (콘텐츠 관리)
INSERT INTO admin_menu (
    menu_name,
    menu_url,
    menu_icon,
    parent_menu_no,
    menu_order,
    menu_level,
    is_active,
    is_visible,
    menu_description,
    created_by,
    created_at
) VALUES 
(
    '공지사항 관리',
    '/admin/notice-management.html',
    'announcement',
    (SELECT menu_no FROM admin_menu WHERE menu_name = '콘텐츠 관리'),
    1,
    2,
    TRUE,
    TRUE,
    '공지사항 관리',
    'admin',
    NOW()
),
(
    '보도자료 관리',
    '/admin/newsroom-management.html',
    'newspaper',
    (SELECT menu_no FROM admin_menu WHERE menu_name = '콘텐츠 관리'),
    2,
    2,
    TRUE,
    TRUE,
    '보도자료 관리',
    'admin',
    NOW()
),
(
    '메뉴 관리',
    '/admin/menu-management.html',
    'menu',
    (SELECT menu_no FROM admin_menu WHERE menu_name = '콘텐츠 관리'),
    3,
    2,
    TRUE,
    TRUE,
    '메뉴 관리',
    'admin',
    NOW()
);

-- 하위 메뉴들 (사용자 관리)
INSERT INTO admin_menu (
    menu_name,
    menu_url,
    menu_icon,
    parent_menu_no,
    menu_order,
    menu_level,
    is_active,
    is_visible,
    menu_description,
    created_by,
    created_at
) VALUES 
(
    '관리자 관리',
    '/admin/admin-management.html',
    'admin_panel_settings',
    (SELECT menu_no FROM admin_menu WHERE menu_name = '사용자 관리'),
    1,
    2,
    TRUE,
    TRUE,
    '관리자 계정 관리',
    'admin',
    NOW()
);

-- 하위 메뉴들 (시스템 관리)
INSERT INTO admin_menu (
    menu_name,
    menu_url,
    menu_icon,
    parent_menu_no,
    menu_order,
    menu_level,
    is_active,
    is_visible,
    menu_description,
    created_by,
    created_at
) VALUES 
(
    '시스템 설정',
    '/admin/system-settings.html',
    'tune',
    (SELECT menu_no FROM admin_menu WHERE menu_name = '시스템 관리'),
    1,
    2,
    TRUE,
    TRUE,
    '시스템 설정',
    'admin',
    NOW()
),
(
    '로그 관리',
    '/admin/log-management.html',
    'history',
    (SELECT menu_no FROM admin_menu WHERE menu_name = '시스템 관리'),
    2,
    2,
    TRUE,
    TRUE,
    '시스템 로그 관리',
    'admin',
    NOW()
);
