-- 어드민 메뉴 테이블 수정: 계층구조 제거
-- 메뉴 레벨과 부모 메뉴 번호 컬럼 제거

-- 1. 기존 외래키 제약조건 제거
ALTER TABLE admin_menu DROP FOREIGN KEY admin_menu_ibfk_1;

-- 2. 부모 메뉴 번호 컬럼 제거
ALTER TABLE admin_menu DROP COLUMN parent_menu_no;

-- 3. 메뉴 레벨 컬럼 제거
ALTER TABLE admin_menu DROP COLUMN menu_level;

-- 4. 기존 인덱스 제거
DROP INDEX idx_parent_menu_no ON admin_menu;
DROP INDEX idx_menu_level ON admin_menu;

-- 5. 기존 데이터 정리 (모든 메뉴를 최상위 메뉴로 변경)
-- 메뉴 순서를 재정렬하여 계층구조 없이 순서대로 배치
UPDATE admin_menu SET menu_order = 1 WHERE menu_name = '대시보드';
UPDATE admin_menu SET menu_order = 2 WHERE menu_name = '콘텐츠 관리';
UPDATE admin_menu SET menu_order = 3 WHERE menu_name = '공지사항 관리';
UPDATE admin_menu SET menu_order = 4 WHERE menu_name = '보도자료 관리';
UPDATE admin_menu SET menu_order = 5 WHERE menu_name = '메뉴 관리';
UPDATE admin_menu SET menu_order = 6 WHERE menu_name = '사용자 관리';
UPDATE admin_menu SET menu_order = 7 WHERE menu_name = '관리자 관리';
UPDATE admin_menu SET menu_order = 8 WHERE menu_name = '시스템 관리';
UPDATE admin_menu SET menu_order = 9 WHERE menu_name = '시스템 설정';
UPDATE admin_menu SET menu_order = 10 WHERE menu_name = '로그 관리';

-- 6. 새로운 인덱스 추가 (메뉴 순서 기반)
CREATE INDEX idx_menu_order ON admin_menu(menu_order);

-- 7. 테이블 구조 확인을 위한 주석
-- 수정된 admin_menu 테이블 구조:
-- - menu_no: 메뉴 번호 (PK)
-- - menu_name: 메뉴명
-- - menu_url: 메뉴 URL
-- - menu_icon: 메뉴 아이콘
-- - menu_order: 메뉴 순서 (계층구조 없이 순서대로)
-- - is_active: 활성화 여부
-- - is_visible: 표시 여부
-- - menu_description: 메뉴 설명
-- - created_by, created_at, updated_by, updated_at: 생성/수정 정보
