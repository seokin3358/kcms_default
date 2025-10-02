-- 보도자료(뉴스룸) 테이블 생성
CREATE TABLE kitms_newsroom (
    newsroom_no BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '보도자료 번호',
    newsroom_title VARCHAR(200) NOT NULL COMMENT '보도자료 제목',
    newsroom_url VARCHAR(500) NOT NULL COMMENT '보도자료 링크',
    newsroom_image VARCHAR(500) COMMENT '보도자료 이미지',
    newsroom_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '상태 (ACTIVE/INACTIVE)',
    created_by VARCHAR(50) COMMENT '생성자',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    INDEX idx_newsroom_status (newsroom_status),
    INDEX idx_newsroom_created_at (created_at),
    INDEX idx_newsroom_title (newsroom_title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='보도자료(뉴스룸) 테이블';

-- 샘플 데이터 삽입
INSERT INTO kitms_newsroom (
    newsroom_title,
    newsroom_url,
    newsroom_image,
    newsroom_status,
    created_by,
    created_at
) VALUES 
(
    '케이원, 2024년 4분기 실적 발표',
    'https://example.com/news/2024-q4-results',
    'newsroom/newsroom-1.jpg',
    'ACTIVE',
    'admin',
    NOW()
),
(
    '케이원, AI 기반 IT 서비스 플랫폼 출시',
    'https://example.com/news/ai-platform-launch',
    'newsroom/newsroom-2.jpg',
    'ACTIVE',
    'admin',
    NOW()
),
(
    '케이원, 클라우드 마이그레이션 서비스 확장',
    'https://example.com/news/cloud-migration-expansion',
    'newsroom/newsroom-3.jpg',
    'ACTIVE',
    'admin',
    NOW()
),
(
    '케이원, 정보보안 인증 획득',
    'https://example.com/news/iso27001-certification',
    'newsroom/newsroom-4.jpg',
    'ACTIVE',
    'admin',
    NOW()
),
(
    '케이원, 신규 사무소 개설',
    'https://example.com/news/new-offices-opening',
    'newsroom/newsroom-5.jpg',
    'ACTIVE',
    'admin',
    NOW()
);
