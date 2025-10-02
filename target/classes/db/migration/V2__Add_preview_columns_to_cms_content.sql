-- CMS 컨텐츠 테이블에 미리보기용 컬럼 추가
ALTER TABLE cms_content 
ADD COLUMN preview_content LONGTEXT COMMENT '미리보기용 페이지 컨텐츠 (HTML)',
ADD COLUMN preview_meta_title VARCHAR(200) COMMENT '미리보기용 메타 타이틀',
ADD COLUMN preview_meta_description TEXT COMMENT '미리보기용 메타 설명',
ADD COLUMN preview_meta_keywords VARCHAR(500) COMMENT '미리보기용 메타 키워드',
ADD COLUMN preview_updated_at TIMESTAMP NULL COMMENT '미리보기 수정일시';

-- 인덱스 추가
CREATE INDEX idx_preview_updated_at ON cms_content(preview_updated_at);














