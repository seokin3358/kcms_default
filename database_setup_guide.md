# KITMS MariaDB 데이터베이스 설정 가이드

## 📋 사전 준비사항

### 1. MariaDB 설치 확인
```bash
# MariaDB 버전 확인
mysql --version

# MariaDB 서비스 상태 확인
# Windows
net start mysql

# Linux/Mac
sudo systemctl status mariadb
```

### 2. MariaDB 접속
```bash
# MariaDB 접속
mysql -u root -p

# 또는 특정 사용자로 접속
mysql -u your_username -p
```

## 🗄️ 데이터베이스 생성 및 설정

### 1. 데이터베이스 생성
```sql
-- MariaDB 접속 후 실행
CREATE DATABASE IF NOT EXISTS testsh CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE testsh;
```

### 2. 스키마 생성
```bash
# SQL 파일 실행
mysql -u root -p testsh < kitms_database_setup.sql

# 또는 MariaDB 내에서 실행
source /path/to/kitms_database_setup.sql;
```

## 🔧 설정 확인

### 1. 테이블 생성 확인
```sql
-- 생성된 테이블 확인
SHOW TABLES LIKE 'kitms_%';

-- 테이블 구조 확인
DESCRIBE kitms_user;
DESCRIBE kitms_notice;
DESCRIBE kitms_attach;
DESCRIBE kitms_login_log;
DESCRIBE kitms_token_storage;
```

### 2. 샘플 데이터 확인
```sql
-- 사용자 데이터 확인
SELECT user_id, user_name, auth_code, enable FROM kitms_user;

-- 공지사항 데이터 확인
SELECT notice_no, notice_title, static_flag, create_dt FROM kitms_notice;
```

## ⚙️ 애플리케이션 설정

### 1. application-dev.yml 설정 확인
```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/testsh?serverTimezone=UTC&useLegacyDatetimeCode=false
    username: root
    password: your_password1
```

### 2. 데이터베이스 연결 테스트
```bash
# 애플리케이션 실행
./mvnw spring-boot:run

# 또는
mvn spring-boot:run
```

## 🚨 문제 해결

### 1. 연결 오류 시
```sql
-- 사용자 권한 확인
SELECT user, host FROM mysql.user WHERE user = 'root';

-- 권한 부여 (필요시)
GRANT ALL PRIVILEGES ON testsh.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

### 2. 테이블 생성 오류 시
```sql
-- 기존 테이블 삭제 (주의!)
DROP TABLE IF EXISTS kitms_token_storage;
DROP TABLE IF EXISTS kitms_attach;
DROP TABLE IF EXISTS kitms_notice;
DROP TABLE IF EXISTS kitms_login_log;
DROP TABLE IF EXISTS kitms_user;
DROP TABLE IF EXISTS sequence_generator;
```

### 3. 인코딩 문제 시
```sql
-- 데이터베이스 인코딩 확인
SHOW VARIABLES LIKE 'character_set%';
SHOW VARIABLES LIKE 'collation%';

-- 테이블 인코딩 변경 (필요시)
ALTER TABLE kitms_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 📊 생성된 테이블 구조

### 1. kitms_user (사용자 테이블)
- **user_no**: 사용자 번호 (PK, AUTO_INCREMENT)
- **user_id**: 사용자 ID (UNIQUE)
- **user_pwd**: 비밀번호 (암호화)
- **user_name**: 사용자 이름
- **auth_code**: 권한 코드
- **organ_no**: 조직 번호
- **enable**: 활성화 여부

### 2. kitms_notice (공지사항 테이블)
- **notice_no**: 공지사항 번호 (PK)
- **notice_title**: 제목
- **notice_content**: 내용
- **static_flag**: 정적 공지 여부
- **create_dt**: 생성일시
- **create_user_id**: 작성자 ID

### 3. kitms_attach (첨부파일 테이블)
- **attach_no**: 첨부파일 번호 (PK)
- **attach_table_name**: 연결 테이블명
- **attach_table_pk**: 연결 테이블 PK
- **attach_file_path**: 파일 경로
- **attach_file_name**: 파일명
- **attach_file**: 파일 내용 (BLOB)

### 4. kitms_login_log (로그인 로그 테이블)
- **log_no**: 로그 번호 (PK)
- **user_id**: 사용자 ID
- **login_success**: 로그인 성공 여부
- **login_dt**: 로그인 시간
- **login_ip**: 로그인 IP
- **login_reason**: 로그인 사유

### 5. kitms_token_storage (토큰 저장소 테이블)
- **user_id**: 사용자 ID (PK)
- **user_token**: JWT 토큰

## 🔐 기본 계정 정보

### 관리자 계정
- **ID**: admin
- **비밀번호**: admin123
- **권한**: 001 (관리자)

### 테스트 계정
- **ID**: testuser
- **비밀번호**: test123
- **권한**: 002 (일반 사용자)

## 📝 추가 설정

### 1. 파일 업로드 경로 설정
```bash
# Linux/Mac
sudo mkdir -p /var/kitms/upload
sudo chmod 755 /var/kitms/upload

# Windows
mkdir C:\var\kitms\upload
```

### 2. 로그 경로 설정
```bash
# Linux/Mac
sudo mkdir -p /var/kitms/log
sudo chmod 755 /var/kitms/log

# Windows
mkdir C:\var\kitms\log
```

이제 KITMS 애플리케이션을 실행하여 데이터베이스 연결을 테스트할 수 있습니다!

