-- Menu Database Design
-- UTF-8 encoding

-- 1. Menu table (Tree structure)
CREATE TABLE kitms_menu (
    menu_id INT PRIMARY KEY AUTO_INCREMENT,
    menu_name VARCHAR(100) NOT NULL,
    menu_url VARCHAR(500),
    parent_id INT NULL,
    menu_level INT NOT NULL DEFAULT 1,
    sort_order INT NOT NULL DEFAULT 0,
    is_active CHAR(1) NOT NULL DEFAULT 'Y',
    is_new_window CHAR(1) NOT NULL DEFAULT 'N',
    menu_description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    
    FOREIGN KEY (parent_id) REFERENCES kitms_menu(menu_id) ON DELETE CASCADE,
    
    INDEX idx_parent_id (parent_id),
    INDEX idx_menu_level (menu_level),
    INDEX idx_sort_order (sort_order),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample data insertion
-- 1Depth menu data
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description) VALUES
('케이원소개', './sub01-01.html', NULL, 1, 1, 'Y', '케이원 회사 소개 관련 메뉴'),
('사업영역', './sub02-01.html', NULL, 1, 2, 'Y', '사업 영역 관련 메뉴'),
('홍보센터', './sub03-01.html', NULL, 1, 3, 'Y', '홍보 및 공지사항 관련 메뉴'),
('채용공고', './sub04-01.html', NULL, 1, 4, 'Y', '채용 관련 메뉴');

-- 2Depth menu data (케이원소개)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description) VALUES
('회사소개', './sub01-01.html', 1, 2, 1, 'Y', '회사 개요 및 비전'),
('케이원연혁', './sub01-02.html', 1, 2, 2, 'Y', '회사 연혁 및 발전 과정'),
('CEO 인사말', './sub01-03.html', 1, 2, 3, 'Y', 'CEO 인사말'),
('윤리경영', './sub01-04.html', 1, 2, 4, 'Y', '윤리경영 정책'),
('조직구성', './sub01-05.html', 1, 2, 5, 'Y', '조직도 및 구성'),
('인증현황', './sub01-06.html', 1, 2, 6, 'Y', '인증 및 수상 현황'),
('오시는 길', './sub01-07.html', 1, 2, 7, 'Y', '회사 위치 및 연락처');

-- 2Depth menu data (사업영역)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description) VALUES
('사업하기', './sub02-01.html', 2, 2, 1, 'Y', '사업 문의 및 상담'),
('문의하기', './sub02-02.html', 2, 2, 2, 'Y', '사업 관련 문의');

-- 2Depth menu data (홍보센터)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description) VALUES
('보도자료', './sub03-01.html', 3, 2, 1, 'Y', '보도자료 및 뉴스'),
('공지사항', './sub03-02.html', 3, 2, 2, 'Y', '공지사항 및 알림');

-- 2Depth menu data (채용공고)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description) VALUES
('채용공고', './sub04-01.html', 4, 2, 1, 'Y', '채용 공고 및 모집'),
('인재상', './sub04-02.html', 4, 2, 2, 'Y', '인재상 및 복리후생');
