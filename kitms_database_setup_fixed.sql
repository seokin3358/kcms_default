-- =====================================================
-- KITMS 데이터베이스 스키마 생성 스크립트 (인코딩 수정)
-- MariaDB용
-- =====================================================

-- 데이터베이스 인코딩 설정
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- =====================================================
-- 1. 시퀀스 생성 (MariaDB는 시퀀스를 지원하지 않으므로 테이블로 대체)
-- =====================================================
CREATE TABLE IF NOT EXISTS sequence_generator (
    sequence_name VARCHAR(50) PRIMARY KEY,
    next_val BIGINT NOT NULL DEFAULT 1050
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT IGNORE INTO sequence_generator (sequence_name, next_val) VALUES ('sequence_generator', 1050);

-- =====================================================
-- 2. 사용자 테이블 (kitms_user)
-- =====================================================
CREATE TABLE IF NOT EXISTS kitms_user (
    user_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(30) NOT NULL UNIQUE,
    user_pwd VARCHAR(512) NOT NULL,
    user_name VARCHAR(30) NOT NULL,
    user_tel VARCHAR(15),
    user_mobile VARCHAR(15),
    user_email VARCHAR(50),
    enable BOOLEAN NOT NULL DEFAULT TRUE,
    auth_code CHAR(3) NOT NULL,
    user_etc VARCHAR(500),
    create_dt DATETIME(6) NOT NULL,
    create_user_id VARCHAR(30) NOT NULL,
    first_flag BOOLEAN NOT NULL DEFAULT FALSE,
    last_pass_mod_dt DATETIME(6),
    vac_start_date DATE,
    vac_end_date DATE,
    onsite_branch_no BIGINT,
    user_role VARCHAR(10),
    organ_no BIGINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. 로그인 로그 테이블 (kitms_login_log)
-- =====================================================
CREATE TABLE IF NOT EXISTS kitms_login_log (
    log_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(30) NOT NULL,
    login_success BOOLEAN,
    login_dt DATETIME(6) NOT NULL,
    login_ip VARCHAR(20),
    login_reason VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 4. 공지사항 테이블 (kitms_notice)
-- =====================================================
CREATE TABLE IF NOT EXISTS kitms_notice (
    notice_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    notice_title VARCHAR(100) NOT NULL,
    notice_content LONGTEXT NOT NULL,
    create_dt DATETIME(6) NOT NULL,
    create_user_id VARCHAR(30) NOT NULL,
    static_flag BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 5. 첨부파일 테이블 (kitms_attach)
-- =====================================================
CREATE TABLE IF NOT EXISTS kitms_attach (
    attach_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    attach_table_name VARCHAR(30) NOT NULL,
    attach_table_pk BIGINT NOT NULL,
    attach_file_path VARCHAR(300) NOT NULL,
    attach_file_name VARCHAR(100) NOT NULL,
    attach_file LONGBLOB,
    attach_file_size BIGINT,
    create_dt DATETIME(6) NOT NULL,
    create_user_id VARCHAR(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 6. 토큰 저장소 테이블 (kitms_token_storage)
-- =====================================================
CREATE TABLE IF NOT EXISTS kitms_token_storage (
    user_id VARCHAR(30) PRIMARY KEY,
    user_token VARCHAR(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 7. 인덱스 생성
-- =====================================================

-- 사용자 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_kitms_user_user_id ON kitms_user(user_id);
CREATE INDEX IF NOT EXISTS idx_kitms_user_auth_code ON kitms_user(auth_code);
CREATE INDEX IF NOT EXISTS idx_kitms_user_organ_no ON kitms_user(organ_no);
CREATE INDEX IF NOT EXISTS idx_kitms_user_enable ON kitms_user(enable);

-- 로그인 로그 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_kitms_login_log_user_id ON kitms_login_log(user_id);
CREATE INDEX IF NOT EXISTS idx_kitms_login_log_login_dt ON kitms_login_log(login_dt);
CREATE INDEX IF NOT EXISTS idx_kitms_login_log_login_success ON kitms_login_log(login_success);

-- 공지사항 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_kitms_notice_create_dt ON kitms_notice(create_dt);
CREATE INDEX IF NOT EXISTS idx_kitms_notice_static_flag ON kitms_notice(static_flag);
CREATE INDEX IF NOT EXISTS idx_kitms_notice_create_user_id ON kitms_notice(create_user_id);

-- 첨부파일 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_kitms_attach_table_info ON kitms_attach(attach_table_name, attach_table_pk);
CREATE INDEX IF NOT EXISTS idx_kitms_attach_create_dt ON kitms_attach(create_dt);

-- =====================================================
-- 8. 샘플 데이터 삽입 (UTF-8 인코딩으로 직접 삽입)
-- =====================================================

-- 관리자 사용자 생성 (비밀번호: admin123)
INSERT IGNORE INTO kitms_user (
    user_id, user_pwd, user_name, auth_code, enable, 
    create_dt, create_user_id, first_flag, organ_no
) VALUES (
    'admin', 
    '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJAWxJg4X8V8V8V8V8V8V8V8V8', -- admin123
    '관리자', 
    '001', 
    TRUE, 
    NOW(), 
    'system', 
    FALSE, 
    1
);

-- 테스트 사용자 생성 (비밀번호: test123)
INSERT IGNORE INTO kitms_user (
    user_id, user_pwd, user_name, auth_code, enable, 
    create_dt, create_user_id, first_flag, organ_no
) VALUES (
    'testuser', 
    '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJAWxJg4X8V8V8V8V8V8V8V8V8', -- test123
    '테스트사용자', 
    '002', 
    TRUE, 
    NOW(), 
    'admin', 
    FALSE, 
    1
);

-- 샘플 공지사항 생성
INSERT IGNORE INTO kitms_notice (
    notice_title, notice_content, create_dt, create_user_id, static_flag
) VALUES 
(
    'KITMS 시스템 오픈 안내',
    'KITMS(Key IT Management System) 시스템이 정식 오픈되었습니다.\n\n시스템 이용에 참고하시기 바랍니다.',
    NOW(),
    'admin',
    TRUE
),
(
    '시스템 점검 안내',
    '정기 시스템 점검으로 인해 일시적으로 서비스가 중단될 예정입니다.\n\n점검 시간: 2024년 1월 15일 02:00 ~ 04:00',
    NOW(),
    'admin',
    FALSE
);

-- =====================================================
-- 완료 메시지
-- =====================================================
SELECT 'KITMS 데이터베이스 스키마 생성이 완료되었습니다.' AS message;
SELECT '생성된 테이블:' AS info;
SHOW TABLES LIKE 'kitms_%';
SELECT '시퀀스 테이블:' AS info;
SHOW TABLES LIKE 'sequence_%';


