-- =====================================================
-- 메뉴 관리 DB 구조 설계
-- =====================================================

-- 1. 메뉴 테이블 (트리 구조)
CREATE TABLE kitms_menu (
    menu_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '메뉴 ID',
    menu_name VARCHAR(100) NOT NULL COMMENT '메뉴명',
    menu_url VARCHAR(500) COMMENT '메뉴 URL',
    parent_id INT NULL COMMENT '부모 메뉴 ID',
    menu_level INT NOT NULL DEFAULT 1 COMMENT '메뉴 레벨 (1: 1depth, 2: 2depth)',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '정렬 순서',
    is_active CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '활성화 여부 (Y/N)',
    is_new_window CHAR(1) NOT NULL DEFAULT 'N' COMMENT '새창 여부 (Y/N)',
    menu_description TEXT COMMENT '메뉴 설명',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    created_by VARCHAR(50) COMMENT '생성자',
    updated_by VARCHAR(50) COMMENT '수정자',
    
    -- 외래키 제약조건
    FOREIGN KEY (parent_id) REFERENCES kitms_menu(menu_id) ON DELETE CASCADE,
    
    -- 인덱스
    INDEX idx_parent_id (parent_id),
    INDEX idx_menu_level (menu_level),
    INDEX idx_sort_order (sort_order),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='메뉴 관리 테이블';

-- 2. 메뉴 권한 테이블 (선택사항 - 권한 관리가 필요한 경우)
CREATE TABLE kitms_menu_auth (
    menu_auth_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '메뉴 권한 ID',
    menu_id INT NOT NULL COMMENT '메뉴 ID',
    auth_code VARCHAR(50) NOT NULL COMMENT '권한 코드',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    
    -- 외래키 제약조건
    FOREIGN KEY (menu_id) REFERENCES kitms_menu(menu_id) ON DELETE CASCADE,
    
    -- 유니크 제약조건
    UNIQUE KEY uk_menu_auth (menu_id, auth_code),
    
    -- 인덱스
    INDEX idx_menu_id (menu_id),
    INDEX idx_auth_code (auth_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='메뉴 권한 관리 테이블';

-- =====================================================
-- 샘플 데이터 삽입
-- =====================================================

-- 1Depth 메뉴 데이터
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description) VALUES
('케이원소개', './sub01-01.html', NULL, 1, 1, 'Y', '케이원 회사 소개 관련 메뉴'),
('사업영역', './sub02-01.html', NULL, 1, 2, 'Y', '사업 영역 관련 메뉴'),
('홍보센터', './sub03-01.html', NULL, 1, 3, 'Y', '홍보 및 공지사항 관련 메뉴'),
('채용공고', './sub04-01.html', NULL, 1, 4, 'Y', '채용 관련 메뉴');

-- 2Depth 메뉴 데이터 (케이원소개)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description) VALUES
('회사소개', './sub01-01.html', 1, 2, 1, 'Y', '회사 개요 및 비전'),
('케이원연혁', './sub01-02.html', 1, 2, 2, 'Y', '회사 연혁 및 발전 과정'),
('CEO 인사말', './sub01-03.html', 1, 2, 3, 'Y', 'CEO 인사말'),
('윤리경영', './sub01-04.html', 1, 2, 4, 'Y', '윤리경영 정책'),
('조직구성', './sub01-05.html', 1, 2, 5, 'Y', '조직도 및 구성'),
('인증현황', './sub01-06.html', 1, 2, 6, 'Y', '인증 및 수상 현황'),
('오시는 길', './sub01-07.html', 1, 2, 7, 'Y', '회사 위치 및 연락처');

-- 2Depth 메뉴 데이터 (사업영역)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description) VALUES
('사업하기', './sub02-01.html', 2, 2, 1, 'Y', '사업 문의 및 상담'),
('문의하기', './sub02-02.html', 2, 2, 2, 'Y', '사업 관련 문의');

-- 2Depth 메뉴 데이터 (홍보센터)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description) VALUES
('보도자료', './sub03-01.html', 3, 2, 1, 'Y', '보도자료 및 뉴스'),
('공지사항', './sub03-02.html', 3, 2, 2, 'Y', '공지사항 및 알림');

-- 2Depth 메뉴 데이터 (채용공고)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description) VALUES
('채용공고', './sub04-01.html', 4, 2, 1, 'Y', '채용 공고 및 모집'),
('인재상', './sub04-02.html', 4, 2, 2, 'Y', '인재상 및 복리후생');

-- =====================================================
-- 트리 구조 조회 쿼리 예시
-- =====================================================

-- 1. 전체 메뉴 트리 구조 조회 (WITH RECURSIVE 사용)
WITH RECURSIVE menu_tree AS (
    -- 루트 메뉴 (1depth)
    SELECT 
        menu_id, 
        menu_name, 
        menu_url, 
        parent_id, 
        menu_level, 
        sort_order,
        CAST(menu_name AS CHAR(1000)) as menu_path,
        0 as depth
    FROM kitms_menu 
    WHERE parent_id IS NULL AND is_active = 'Y'
    
    UNION ALL
    
    -- 자식 메뉴 (2depth)
    SELECT 
        m.menu_id, 
        m.menu_name, 
        m.menu_url, 
        m.parent_id, 
        m.menu_level, 
        m.sort_order,
        CONCAT(mt.menu_path, ' > ', m.menu_name) as menu_path,
        mt.depth + 1
    FROM kitms_menu m
    INNER JOIN menu_tree mt ON m.parent_id = mt.menu_id
    WHERE m.is_active = 'Y'
)
SELECT 
    menu_id,
    menu_name,
    menu_url,
    parent_id,
    menu_level,
    sort_order,
    menu_path,
    depth
FROM menu_tree 
ORDER BY sort_order, menu_id;

-- 2. 특정 부모 메뉴의 자식 메뉴 조회
SELECT 
    menu_id,
    menu_name,
    menu_url,
    sort_order
FROM kitms_menu 
WHERE parent_id = 1 
  AND is_active = 'Y'
ORDER BY sort_order;

-- 3. 메뉴 계층 구조 조회 (부모-자식 관계)
SELECT 
    p.menu_name as parent_menu,
    c.menu_name as child_menu,
    c.menu_url,
    c.sort_order
FROM kitms_menu p
LEFT JOIN kitms_menu c ON p.menu_id = c.parent_id
WHERE p.menu_level = 1 
  AND p.is_active = 'Y'
ORDER BY p.sort_order, c.sort_order;

-- =====================================================
-- 메뉴 관리용 유틸리티 쿼리
-- =====================================================

-- 메뉴 활성화/비활성화
UPDATE kitms_menu SET is_active = 'N' WHERE menu_id = ?;

-- 메뉴 정렬 순서 변경
UPDATE kitms_menu SET sort_order = ? WHERE menu_id = ?;

-- 메뉴 URL 변경
UPDATE kitms_menu SET menu_url = ? WHERE menu_id = ?;

-- 특정 메뉴와 모든 하위 메뉴 조회
WITH RECURSIVE submenu_tree AS (
    SELECT menu_id, menu_name, parent_id, menu_level, sort_order
    FROM kitms_menu 
    WHERE menu_id = ? -- 특정 메뉴 ID
    
    UNION ALL
    
    SELECT m.menu_id, m.menu_name, m.parent_id, m.menu_level, m.sort_order
    FROM kitms_menu m
    INNER JOIN submenu_tree st ON m.parent_id = st.menu_id
)
SELECT * FROM submenu_tree ORDER BY menu_level, sort_order;
