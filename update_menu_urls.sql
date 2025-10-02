-- 메뉴 URL 업데이트 스크립트
-- 파일명 형식을 sub01-01.html에서 sub0101.html로 변경

-- 1Depth 메뉴 URL 업데이트
UPDATE kitms_menu SET menu_url = './sub0101.html' WHERE menu_name = '케이원소개' AND menu_level = 1;
UPDATE kitms_menu SET menu_url = './sub0201.html' WHERE menu_name = '사업영역' AND menu_level = 1;
UPDATE kitms_menu SET menu_url = './sub0301.html' WHERE menu_name = '홍보센터' AND menu_level = 1;
UPDATE kitms_menu SET menu_url = './sub0401.html' WHERE menu_name = '채용공고' AND menu_level = 1;

-- 2Depth 메뉴 URL 업데이트 (케이원소개)
UPDATE kitms_menu SET menu_url = './sub0101.html' WHERE menu_name = '회사소개' AND menu_level = 2;
UPDATE kitms_menu SET menu_url = './sub0102.html' WHERE menu_name = '케이원연혁' AND menu_level = 2;
UPDATE kitms_menu SET menu_url = './sub0103.html' WHERE menu_name = 'CEO 인사말' AND menu_level = 2;
UPDATE kitms_menu SET menu_url = './sub0104.html' WHERE menu_name = '윤리경영' AND menu_level = 2;
UPDATE kitms_menu SET menu_url = './sub0105.html' WHERE menu_name = '조직구성' AND menu_level = 2;
UPDATE kitms_menu SET menu_url = './sub0106.html' WHERE menu_name = '인증현황' AND menu_level = 2;
UPDATE kitms_menu SET menu_url = './sub0107.html' WHERE menu_name = '오시는 길' AND menu_level = 2;

-- 2Depth 메뉴 URL 업데이트 (사업영역)
UPDATE kitms_menu SET menu_url = './sub0201.html' WHERE menu_name = '사업하기' AND menu_level = 2;
UPDATE kitms_menu SET menu_url = './sub0202.html' WHERE menu_name = '문의하기' AND menu_level = 2;

-- 2Depth 메뉴 URL 업데이트 (홍보센터)
UPDATE kitms_menu SET menu_url = './sub0301.html' WHERE menu_name = '보도자료' AND menu_level = 2;
UPDATE kitms_menu SET menu_url = './sub0302.html' WHERE menu_name = '공지사항' AND menu_level = 2;

-- 2Depth 메뉴 URL 업데이트 (채용공고)
UPDATE kitms_menu SET menu_url = './sub0401.html' WHERE menu_name = '채용공고' AND menu_level = 2;
UPDATE kitms_menu SET menu_url = './sub0402.html' WHERE menu_name = '인재상' AND menu_level = 2;

-- 업데이트 결과 확인
SELECT menu_id, menu_name, menu_url, menu_level, sort_order 
FROM kitms_menu 
WHERE is_active = 'Y' 
ORDER BY menu_level, sort_order, menu_id;

