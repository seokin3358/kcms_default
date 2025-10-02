-- 테스트용 메뉴 데이터 생성
-- 기존 메뉴 데이터 삭제 (테스트용)
DELETE FROM kitms_menu;

-- 1Depth 메뉴 데이터
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description, created_at, updated_at) VALUES
('케이원소개', './sub0101.html', NULL, 1, 1, 'Y', '케이원 회사 소개 관련 메뉴', NOW(), NOW()),
('사업영역', './sub0201.html', NULL, 1, 2, 'Y', '사업 영역 관련 메뉴', NOW(), NOW()),
('홍보센터', './sub0301.html', NULL, 1, 3, 'Y', '홍보 및 공지사항 관련 메뉴', NOW(), NOW()),
('채용공고', './sub0401.html', NULL, 1, 4, 'Y', '채용 관련 메뉴', NOW(), NOW());

-- 2Depth 메뉴 데이터 (케이원소개)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description, created_at, updated_at) VALUES
('회사소개', './sub0101.html', 1, 2, 1, 'Y', '회사 개요 및 비전', NOW(), NOW()),
('케이원연혁', './sub0102.html', 1, 2, 2, 'Y', '회사 연혁 및 발전 과정', NOW(), NOW()),
('CEO 인사말', './sub0103.html', 1, 2, 3, 'Y', 'CEO 인사말', NOW(), NOW()),
('윤리경영', './sub0104.html', 1, 2, 4, 'Y', '윤리경영 정책', NOW(), NOW()),
('조직구성', './sub0105.html', 1, 2, 5, 'Y', '조직도 및 구성', NOW(), NOW()),
('인증현황', './sub0106.html', 1, 2, 6, 'Y', '인증 및 수상 현황', NOW(), NOW()),
('오시는 길', './sub0107.html', 1, 2, 7, 'Y', '회사 위치 및 연락처', NOW(), NOW());

-- 2Depth 메뉴 데이터 (사업영역)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description, created_at, updated_at) VALUES
('사업하기', './sub0201.html', 2, 2, 1, 'Y', '사업 문의 및 상담', NOW(), NOW()),
('문의하기', './sub0202.html', 2, 2, 2, 'Y', '사업 관련 문의', NOW(), NOW());

-- 2Depth 메뉴 데이터 (홍보센터)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description, created_at, updated_at) VALUES
('보도자료', './sub0301.html', 3, 2, 1, 'Y', '보도자료 및 뉴스', NOW(), NOW()),
('공지사항', './sub0302.html', 3, 2, 2, 'Y', '공지사항 및 알림', NOW(), NOW());

-- 2Depth 메뉴 데이터 (채용공고)
INSERT INTO kitms_menu (menu_name, menu_url, parent_id, menu_level, sort_order, is_active, menu_description, created_at, updated_at) VALUES
('채용공고', './sub0401.html', 4, 2, 1, 'Y', '채용 공고 및 모집', NOW(), NOW()),
('인재상', './sub0402.html', 4, 2, 2, 'Y', '인재상 및 복리후생', NOW(), NOW());

-- 메뉴 데이터 확인
SELECT COUNT(*) as total_menus FROM kitms_menu;
SELECT * FROM kitms_menu ORDER BY menu_level, sort_order;









