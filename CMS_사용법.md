# CMS 시스템 사용법

## 개요
이 CMS 시스템은 기존의 정적 HTML 페이지를 데이터베이스 기반의 동적 컨텐츠 관리 시스템으로 변환한 것입니다.

## 시스템 구조

### 1. 데이터베이스 테이블
- **테이블명**: `cms_content`
- **주요 컬럼**:
  - `page_code`: 페이지 식별 코드 (예: sub0101)
  - `page_title`: 페이지 제목
  - `page_content`: 페이지 컨텐츠 (LONGTEXT, HTML 형식)
  - `meta_title`, `meta_description`, `meta_keywords`: SEO 메타 정보
  - `status`: 활성화 상태 (ACTIVE/INACTIVE)

### 2. 백엔드 API
- **기본 URL**: `/api/cms`
- **주요 엔드포인트**:
  - `GET /api/cms/content/{pageCode}`: 특정 페이지 컨텐츠 조회
  - `GET /api/cms/contents`: 모든 활성화된 컨텐츠 조회
  - `POST /api/cms/content`: 새 컨텐츠 생성
  - `PUT /api/cms/content/{id}`: 컨텐츠 수정
  - `DELETE /api/cms/content/{id}`: 컨텐츠 삭제 (상태를 INACTIVE로 변경)

### 3. 프론트엔드 파일
- **cms-template.html**: 공통 템플릿 페이지 (헤더/푸터 포함)
- **cms-test.html**: CMS 시스템 테스트 페이지

## 사용 방법

### 1. 데이터베이스 설정
1. `src/main/resources/db/migration/V1__Create_cms_content_table.sql` 파일을 실행하여 테이블 생성
2. 테스트 데이터가 자동으로 삽입됩니다 (sub0101 페이지)

### 2. CMS 페이지 접근
```
http://localhost:8080/cms-template.html?page=sub0101
```
- `page` 파라미터로 원하는 페이지 코드를 지정
- 기본값은 `sub0101`

### 3. 테스트 페이지 접근
```
http://localhost:8080/cms-test.html
```
- API 연결 테스트
- 컨텐츠 조회 테스트
- CMS 페이지 미리보기
- 새 컨텐츠 생성 테스트

## 컨텐츠 관리

### 1. 기존 페이지를 CMS로 변환
1. 기존 HTML 파일의 `<body>` 내부 컨텐츠를 추출
2. 데이터베이스에 새 레코드 생성:
   ```sql
   INSERT INTO cms_content (page_code, page_title, page_content, status, created_by) 
   VALUES ('새페이지코드', '페이지제목', 'HTML컨텐츠', 'ACTIVE', '관리자');
   ```

### 2. 새 컨텐츠 생성
1. 테스트 페이지에서 "컨텐츠 관리 테스트" 섹션 사용
2. 또는 API를 직접 호출:
   ```javascript
   fetch('/api/cms/content', {
       method: 'POST',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify({
           pageCode: '새페이지코드',
           pageTitle: '페이지제목',
           pageContent: 'HTML컨텐츠',
           status: 'ACTIVE',
           createdBy: '관리자'
       })
   });
   ```

### 3. 컨텐츠 수정
1. 데이터베이스에서 직접 수정
2. 또는 API를 통해 수정:
   ```javascript
   fetch('/api/cms/content/{id}', {
       method: 'PUT',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify(수정된컨텐츠객체)
   });
   ```

## 장점

### 1. 유지보수성 향상
- 헤더/푸터가 공통 템플릿으로 관리됨
- 컨텐츠 수정 시 데이터베이스만 변경하면 됨
- 코드 중복 제거

### 2. 확장성
- 새 페이지 추가가 간단함
- 메타 정보 관리 가능
- 버전 관리 가능 (생성일/수정일 추적)

### 3. 관리 편의성
- 웹 인터페이스를 통한 컨텐츠 관리 가능
- API를 통한 자동화 가능
- 상태 관리 (활성화/비활성화)

## 주의사항

1. **기존 파일 보존**: 원본 HTML 파일들은 그대로 유지됩니다
2. **CSS/JS 호환성**: 기존 스타일과 스크립트가 그대로 적용됩니다
3. **SEO**: 메타 정보를 데이터베이스에서 관리할 수 있습니다
4. **보안**: 현재는 기본적인 CRUD 기능만 제공되며, 인증/권한 관리는 별도 구현이 필요합니다

## 다음 단계

1. **관리자 인터페이스**: 웹 기반 컨텐츠 편집기 개발
2. **인증/권한**: 관리자 로그인 및 권한 관리
3. **파일 업로드**: 이미지/파일 관리 기능
4. **버전 관리**: 컨텐츠 변경 이력 추적
5. **캐싱**: 성능 최적화를 위한 캐싱 시스템

## 문제 해결

### API 연결 오류
1. 서버가 실행 중인지 확인
2. 데이터베이스 연결 상태 확인
3. 브라우저 개발자 도구에서 네트워크 탭 확인

### 컨텐츠가 표시되지 않음
1. 페이지 코드가 정확한지 확인
2. 데이터베이스에 해당 레코드가 있는지 확인
3. status가 'ACTIVE'인지 확인

### 스타일이 적용되지 않음
1. CSS 파일 경로가 올바른지 확인
2. 브라우저 캐시 클리어
3. 네트워크 탭에서 CSS 파일 로딩 상태 확인
