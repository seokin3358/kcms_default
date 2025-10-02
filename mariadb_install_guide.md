# MariaDB 설치 및 KITMS 데이터베이스 설정 가이드

## 🚀 MariaDB 설치 방법

### 방법 1: 공식 웹사이트에서 다운로드 (권장)

1. **MariaDB 공식 웹사이트 방문**
   - https://mariadb.org/download/
   - Windows용 MariaDB Community Server 다운로드

2. **설치 파일 실행**
   - 다운로드한 `.msi` 파일 실행
   - 설치 마법사 따라하기
   - **중요**: root 비밀번호를 `password`로 설정

3. **설치 확인**
   ```cmd
   mysql --version
   ```

### 방법 2: Chocolatey 사용 (관리자 권한 필요)

```powershell
# PowerShell을 관리자 권한으로 실행
Set-ExecutionPolicy Bypass -Scope Process -Force
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# MariaDB 설치
choco install mariadb
```

### 방법 3: XAMPP 설치

1. **XAMPP 다운로드**
   - https://www.apachefriends.org/download.html
   - Windows용 XAMPP 다운로드

2. **설치 및 실행**
   - XAMPP Control Panel에서 MySQL 시작
   - 기본 포트: 3306
   - 기본 비밀번호: (없음)

## 🗄️ 데이터베이스 생성

### 1. MariaDB 접속
```cmd
mysql -u root -p
# 비밀번호 입력: password
```

### 2. 데이터베이스 생성
```sql
CREATE DATABASE IF NOT EXISTS testsh CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE testsh;
```

### 3. 스키마 생성
```cmd
# SQL 파일 실행
mysql -u root -p testsh < kitms_database_setup.sql
```

## 🔧 빠른 설치 스크립트

### Windows용 자동 설치 스크립트
```powershell
# PowerShell을 관리자 권한으로 실행
# MariaDB 자동 설치 및 설정

# 1. Chocolatey 설치
Set-ExecutionPolicy Bypass -Scope Process -Force
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# 2. MariaDB 설치
choco install mariadb -y

# 3. MariaDB 서비스 시작
net start mysql

# 4. 데이터베이스 생성
mysql -u root -e "CREATE DATABASE IF NOT EXISTS testsh CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 5. 스키마 생성
mysql -u root -p testsh < kitms_database_setup.sql
```

## ⚡ 즉시 실행 가능한 방법

### Docker Desktop 사용 (가장 간단)

1. **Docker Desktop 설치**
   - https://www.docker.com/products/docker-desktop/
   - Windows용 Docker Desktop 다운로드 및 설치

2. **MariaDB 컨테이너 실행**
   ```cmd
   docker run --name kitms-mariadb -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=testsh -p 3306:3306 -d mariadb:10.6
   ```

3. **데이터베이스 스키마 생성**
   ```cmd
   docker exec -i kitms-mariadb mysql -u root -ppassword testsh < kitms_database_setup.sql
   ```

## 🔍 설치 확인

### 1. MariaDB 서비스 상태 확인
```cmd
# Windows 서비스 확인
sc query mysql

# 또는
net start | findstr mysql
```

### 2. 데이터베이스 연결 테스트
```cmd
mysql -u root -p -e "SHOW DATABASES;"
```

### 3. KITMS 테이블 확인
```sql
USE testsh;
SHOW TABLES LIKE 'kitms_%';
```

## 🚨 문제 해결

### 1. MariaDB 서비스 시작 안됨
```cmd
# 서비스 수동 시작
net start mysql

# 또는
sc start mysql
```

### 2. 포트 충돌 (3306 포트 사용 중)
```cmd
# 포트 사용 확인
netstat -an | findstr :3306

# 다른 포트로 실행 (예: 3307)
mysql -u root -p -P 3307
```

### 3. 비밀번호 오류
```sql
-- 비밀번호 재설정
ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';
FLUSH PRIVILEGES;
```

## 📝 다음 단계

1. **MariaDB 설치 완료 후**
2. **데이터베이스 스키마 생성**
3. **KITMS 애플리케이션 실행**
4. **데이터베이스 연결 테스트**

설치가 완료되면 다시 터미널에서 데이터베이스를 생성해드리겠습니다!


