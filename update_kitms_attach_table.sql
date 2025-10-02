-- kitms_attach 테이블에 썸네일 컬럼 추가
-- 이 SQL문을 실행하여 데이터베이스 스키마를 업데이트하세요

USE testsh;

-- 1. is_thumbnail 컬럼 추가
ALTER TABLE kitms_attach ADD COLUMN is_thumbnail BOOLEAN DEFAULT FALSE;

-- 2. 기존 데이터에 대해 첫 번째 이미지를 썸네일로 설정 (선택사항)
-- 각 공지사항별로 첫 번째 이미지를 썸네일로 설정
UPDATE kitms_attach 
SET is_thumbnail = TRUE 
WHERE attach_no IN (
    SELECT MIN(attach_no) 
    FROM kitms_attach 
    WHERE attach_table_name = 'KITMS_NOTICE' 
    GROUP BY attach_table_pk
);

-- 3. 컬럼이 정상적으로 추가되었는지 확인
DESCRIBE kitms_attach;

-- 4. 업데이트된 데이터 확인
SELECT 
    attach_no,
    attach_table_name,
    attach_table_pk,
    attach_file_name,
    is_thumbnail
FROM kitms_attach 
WHERE attach_table_name = 'KITMS_NOTICE'
ORDER BY attach_table_pk, attach_no;





