# KITMS 프로젝트 바이브 코딩 컨텍스트 프롬프트

## 🎯 프로젝트 개요

**KITMS (K-One Information Technology Management System)**는 케이원의 기업 정보 관리 시스템입니다. JHipster 8.1.0 기반의 Spring Boot 3.2.0 애플리케이션으로, 정적 HTML 웹사이트를 동적 CMS 시스템으로 변환한 프로젝트입니다.

## 🏗️ 기술 스택 및 아키텍처

### 백엔드 기술 스택
- **Java 17** + **Spring Boot 3.2.0**
- **JHipster 8.1.0** (생성기 기반)
- **MariaDB** (데이터베이스)
- **JPA/Hibernate 6.3.1** + **MyBatis 3.0.3** (하이브리드 ORM)
- **QueryDSL 5.0.0** (동적 쿼리)
- **Liquibase 4.24.0** (데이터베이스 마이그레이션)
- **Spring Security** + **JWT** (인증/인가)
- **Thymeleaf** (템플릿 엔진)
- **Undertow** (웹 서버)

### 프론트엔드 기술 스택
- **Vanilla JavaScript** (프레임워크 없음)
- **HTML5** + **CSS3**
- **Pretendard, RedHat Display** 폰트
- **Material Symbols** 아이콘

### 개발 도구
- **Maven 3.2.5** (빌드 도구)
- **Node.js 18.18.2** + **NPM 10.2.4**
- **Lombok 1.18.28**
- **MapStruct 1.5.5** (DTO 매핑)
- **Apache POI 5.2.2** (엑셀 처리)
- **ZXing 3.1.0** (QR 코드)

## 📊 핵심 도메인 모델

### 1. 사용자 관리 (KitmsUser)
```java
// 주요 필드
- userNo: 사용자 번호 (PK)
- userId: 사용자 ID (로그인용)
- userPwd: 암호화된 비밀번호
- userName: 사용자 이름
- authCode: 권한 코드 (001: 메인관리자, 002: 서브관리자)
- enable: 계정 활성화 여부
- organNo: 소속 조직 번호
- userRole: 사용자 역할
```

### 2. 공지사항 관리 (KitmsNotice)
```java
// 주요 필드
- noticeNo: 공지사항 번호 (PK)
- noticeTitle: 공지사항 제목
- noticeContent: 공지사항 내용 (HTML)
- createUserId: 작성자 ID
- createDt: 작성일시
- staticFlag: 고정 공지 여부
```

### 3. 메뉴 관리 (KitmsMenu)
```java
// 주요 필드
- menuId: 메뉴 ID (PK)
- menuName: 메뉴명
- menuUrl: 메뉴 링크 URL
- parentId: 부모 메뉴 ID (트리 구조)
- menuLevel: 메뉴 레벨
- sortOrder: 정렬 순서
- isActive: 활성화 여부
```

### 4. CMS 컨텐츠 (CmsContent)
```java
// 주요 필드
- id: 컨텐츠 ID (PK)
- pageCode: 페이지 식별 코드
- pageTitle: 페이지 제목
- pageContent: 페이지 컨텐츠 (HTML)
- metaTitle/metaDescription/metaKeywords: SEO 메타 정보
- status: 상태 (ACTIVE/INACTIVE)
```

### 5. 기타 엔티티
- **KitmsAttach**: 파일 첨부 관리
- **KitmsNewsroom**: 뉴스룸 관리
- **KitmsLoginLog**: 로그인 로그
- **AdminMenu**: 관리자 메뉴
- **KitmsTokenStorage**: JWT 토큰 저장

## 🔐 보안 및 인증

### 인증 방식
- **JWT 토큰 기반 인증**
- **Spring Security** 필터 체인
- **BCrypt** 비밀번호 암호화
- **CSRF 보호** (웹 폼용)
- **CORS 설정**

### 권한 체계
- **001**: 메인관리자 (모든 권한)
- **002**: 서브관리자 (제한된 권한)
- **기본 사용자**: 읽기 전용

### 보안 설정
```java
// API 엔드포인트 보안
- /api/kitms-login: 인증 불필요
- /api/secure-images/{token}: 토큰 기반 접근
- /api/**/*: JWT 토큰 필요
- /admin/**/*: 관리자 인증 필요
```

## 🗄️ 데이터베이스 구조

### 주요 테이블
```sql
-- 사용자 테이블
kitms_user (user_no, user_id, user_pwd, user_name, auth_code, enable, organ_no)

-- 공지사항 테이블  
kitms_notice (notice_no, notice_title, notice_content, create_user_id, create_dt, static_flag)

-- 메뉴 테이블
kitms_menu (menu_id, menu_name, menu_url, parent_id, menu_level, sort_order, is_active)

-- CMS 컨텐츠 테이블
cms_content (id, page_code, page_title, page_content, meta_title, status)

-- 첨부파일 테이블
kitms_attach (attach_no, attach_table_name, attach_table_pk, attach_file_path, attach_file_name)

-- 로그인 로그 테이블
kitms_login_log (log_no, user_id, login_success, login_dt, login_ip)
```

### 데이터베이스 설정
- **MariaDB 10.3+**
- **UTF8MB4** 인코딩
- **Asia/Seoul** 타임존
- **Liquibase** 마이그레이션 관리

## 🚀 API 엔드포인트 구조

### 인증 API
```http
POST /api/kitms-login          # 로그인
GET  /api/kitms-users/current  # 현재 사용자 정보
```

### 사용자 관리 API
```http
GET    /api/kitms-users/all     # 모든 사용자 조회
GET    /api/kitms-users/{userId} # 특정 사용자 조회
POST   /api/kitms-users         # 사용자 생성
PUT    /api/kitms-users         # 사용자 수정
DELETE /api/kitms-users/{userId} # 사용자 삭제
```

### 공지사항 API
```http
GET    /api/kitms-notices       # 공지사항 목록 (페이지네이션)
GET    /api/kitms-notices/{id}  # 특정 공지사항 조회
POST   /api/kitms-notices       # 공지사항 생성
PUT    /api/kitms-notices/{id}  # 공지사항 수정
DELETE /api/kitms-notices/{id}  # 공지사항 삭제
```

### 메뉴 관리 API
```http
GET /api/kitms-menus/active     # 활성 메뉴 조회
GET /api/admin-menus            # 관리자 메뉴 조회
POST /api/admin-menus           # 관리자 메뉴 생성
```

### CMS API
```http
GET  /api/cms/content/{pageCode} # 페이지 컨텐츠 조회
GET  /api/cms/contents          # 모든 활성 컨텐츠 조회
POST /api/cms/content           # 컨텐츠 생성
PUT  /api/cms/content/{id}      # 컨텐츠 수정
```

### 파일 관리 API
```http
POST /api/kitms-attaches/upload # 파일 업로드
GET  /api/kitms-attaches/{id}/download # 파일 다운로드
GET  /api/secure-images/{token} # 보안 이미지 조회
```

## 🎨 프론트엔드 구조

### 정적 리소스
```
src/main/resources/static/
├── admin/           # 관리자 페이지
├── fonts/           # 웹폰트
├── images/          # 이미지 리소스
├── js/              # JavaScript 파일
├── style/           # CSS/SCSS 파일
├── main.html        # 메인 페이지
├── header.html      # 공통 헤더
├── footer.html      # 공통 푸터
└── notice-section.html # 공지사항 섹션
```

### 템플릿 파일
```
src/main/resources/templates/
├── cms-page.html      # CMS 페이지 템플릿
├── cms-template.html  # CMS 공통 템플릿
├── sub0301.html       # 서브페이지들
├── sub0302.html
└── sub0303.html
```

## 🔧 개발 환경 설정

### 필수 요구사항
- **Java 17**
- **Maven 3.2.5+**
- **Node.js 18.18.2**
- **MariaDB 10.3+**
- **Git**

### 개발 서버 실행
```bash
# 백엔드 실행
./mvnw spring-boot:run

# 프론트엔드 빌드 (필요시)
npm run webapp:build

# 프로덕션 빌드
./mvnw -Pprod clean verify
```

### 데이터베이스 설정
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/testsh?serverTimezone=UTC
    username: root
    password: your_password
```

## 📝 코딩 컨벤션 및 패턴

### 패키지 구조
```
com.kone.kitms/
├── config/          # 설정 클래스
├── domain/          # JPA 엔티티
├── repository/      # JPA 리포지토리
├── service/         # 비즈니스 로직
├── web/rest/        # REST 컨트롤러
├── security/        # 보안 관련
├── mybatis/         # MyBatis 매퍼
└── mapper/          # 매퍼 인터페이스
```

### 네이밍 컨벤션
- **엔티티**: `KitmsUser`, `KitmsNotice`
- **서비스**: `KitmsUserService`, `KitmsNoticeService`
- **컨트롤러**: `KitmsUserResource`, `KitmsNoticeResource`
- **DTO**: `KitmsUserDTO`, `KitmsNoticeDTO`
- **테이블**: `kitms_user`, `kitms_notice`

### 응답 형식
```json
{
  "statusCode": 200,
  "message": "요청 처리 성공",
  "data": { ... }
}
```

## 🚨 주의사항 및 제약사항

### 보안 관련
- **JWT 토큰 만료 시간**: 24시간
- **파일 업로드 크기 제한**: 70MB
- **CSRF 토큰**: 웹 폼에서 필수
- **관리자 권한**: API 접근 시 필수

### 성능 관련
- **JPA N+1 문제**: `@EntityGraph` 또는 `JOIN FETCH` 사용
- **대용량 데이터**: 페이지네이션 필수
- **이미지 처리**: 토큰 기반 보안 접근
- **캐싱**: EhCache 사용

### 데이터베이스 관련
- **트랜잭션**: `@Transactional` 적절히 사용
- **마이그레이션**: Liquibase로 관리
- **인덱스**: 자주 조회되는 컬럼에 설정
- **백업**: 정기적인 데이터베이스 백업 필요

## 🎯 바이브 코딩 시 핵심 포인트

### 1. 기존 코드 분석 우선
- **엔티티 관계** 파악
- **API 엔드포인트** 구조 이해
- **보안 설정** 확인
- **데이터베이스 스키마** 검토

### 2. 일관성 유지
- **네이밍 컨벤션** 준수
- **응답 형식** 통일
- **에러 처리** 표준화
- **로깅** 일관성

### 3. 보안 고려사항
- **JWT 토큰** 검증
- **권한 체크** 필수
- **입력값 검증** 강화
- **SQL 인젝션** 방지

### 4. 성능 최적화
- **N+1 문제** 해결
- **페이지네이션** 적용
- **캐싱** 활용
- **인덱스** 최적화

## 📋 바이브 코딩 시작 시 체크리스트

### 프로젝트 이해도 확인
- [ ] 프로젝트 전체 구조 파악
- [ ] 주요 도메인 모델 이해
- [ ] API 엔드포인트 구조 파악
- [ ] 데이터베이스 스키마 검토
- [ ] 보안 설정 확인

### 개발 환경 준비
- [ ] Java 17 설치 확인
- [ ] Maven 설정 확인
- [ ] MariaDB 연결 확인
- [ ] IDE 설정 완료
- [ ] Git 상태 확인

### 코딩 시작 전
- [ ] 요구사항 명확화
- [ ] 기존 코드 패턴 분석
- [ ] 테스트 케이스 작성
- [ ] 에러 처리 방안 수립
- [ ] 성능 고려사항 검토

---

**이 컨텍스트 프롬프트를 바이브 코딩 시작 시 제공하면, AI가 프로젝트의 전체적인 구조와 요구사항을 빠르게 이해하고 더 정확하고 일관성 있는 코드를 생성할 수 있습니다.**

**문서 버전**: 1.0  
**마지막 업데이트**: 2025년 1월 26일  
**작성자**: KITMS Development Team
