# KITMS API 문서

## 📋 API 개요

KITMS 시스템의 REST API 엔드포인트와 사용법을 정리한 문서입니다.

**Base URL**: `http://localhost:8080/api`

---

## 🔐 인증

### JWT 토큰 기반 인증
모든 API 요청에는 JWT 토큰이 필요합니다.

```http
Authorization: Bearer <JWT_TOKEN>
```

### 토큰 발급
```http
POST /api/kitms-login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123",
  "rememberMe": false
}
```

**응답**:
```json
{
  "statusCode": 200,
  "message": "로그인 성공",
  "data": {
    "idToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400
  }
}
```

---

## 👥 사용자 관리 API

### 1. 모든 사용자 조회
```http
GET /api/kitms-users/all
Authorization: Bearer <JWT_TOKEN>
```

**응답**:
```json
{
  "statusCode": 200,
  "message": "사용자 목록 조회 성공",
  "data": [
    {
      "userNo": 1,
      "userId": "admin",
      "userName": "관리자",
      "userEmail": "admin@kone.com",
      "authCode": "001",
      "enable": true,
      "organNo": 1
    }
  ]
}
```

### 2. 특정 사용자 조회
```http
GET /api/kitms-users/{userId}
Authorization: Bearer <JWT_TOKEN>
```

### 3. 사용자 생성
```http
POST /api/kitms-users
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "userId": "newuser",
  "userName": "새 사용자",
  "password": "password123",
  "userEmail": "newuser@kone.com",
  "userPhone": "010-1234-5678",
  "userRole": "002",
  "useFlag": "1"
}
```

### 4. 사용자 수정
```http
PUT /api/kitms-users
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "userId": "existinguser",
  "userName": "수정된 사용자",
  "userEmail": "updated@kone.com",
  "userRole": "001",
  "useFlag": "1"
}
```

### 5. 사용자 삭제
```http
DELETE /api/kitms-users/{userId}
Authorization: Bearer <JWT_TOKEN>
```

### 6. 현재 사용자 정보 조회
```http
GET /api/kitms-users/current
Authorization: Bearer <JWT_TOKEN>
```

---

## 📢 공지사항 관리 API

### 1. 공지사항 목록 조회
```http
GET /api/kitms-notices
Authorization: Bearer <JWT_TOKEN>
```

**쿼리 파라미터**:
- `page`: 페이지 번호 (기본값: 0)
- `size`: 페이지 크기 (기본값: 20)
- `sort`: 정렬 기준 (예: `createdAt,desc`)

**응답**:
```json
{
  "statusCode": 200,
  "message": "공지사항 목록 조회 성공",
  "data": {
    "content": [
      {
        "noticeNo": 1,
        "noticeTitle": "공지사항 제목",
        "noticeContent": "<p>공지사항 내용</p>",
        "noticeStatus": "ACTIVE",
        "createdAt": "2025-01-26T10:00:00",
        "createdBy": "admin"
      }
    ],
    "totalElements": 10,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
}
```

### 2. 특정 공지사항 조회
```http
GET /api/kitms-notices/{noticeNo}
Authorization: Bearer <JWT_TOKEN>
```

### 3. 공지사항 생성
```http
POST /api/kitms-notices
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "noticeTitle": "새 공지사항",
  "noticeContent": "<p>공지사항 내용입니다.</p>",
  "noticeStatus": "ACTIVE"
}
```

### 4. 공지사항 수정
```http
PUT /api/kitms-notices/{noticeNo}
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "noticeTitle": "수정된 공지사항",
  "noticeContent": "<p>수정된 내용입니다.</p>",
  "noticeStatus": "ACTIVE"
}
```

### 5. 공지사항 삭제
```http
DELETE /api/kitms-notices/{noticeNo}
Authorization: Bearer <JWT_TOKEN>
```

---

## 📰 뉴스룸 관리 API

### 1. 뉴스룸 목록 조회
```http
GET /api/kitms-newsrooms
Authorization: Bearer <JWT_TOKEN>
```

### 2. 뉴스룸 생성
```http
POST /api/kitms-newsrooms
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "newsroomTitle": "뉴스 제목",
  "newsroomUrl": "https://example.com/news",
  "newsroomImage": "/images/news.jpg",
  "newsroomStatus": "ACTIVE"
}
```

### 3. 뉴스룸 수정
```http
PUT /api/kitms-newsrooms/{newsroomNo}
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

### 4. 뉴스룸 삭제
```http
DELETE /api/kitms-newsrooms/{newsroomNo}
Authorization: Bearer <JWT_TOKEN>
```

---

## 🖼️ 보안 이미지 API

### 1. 이미지 토큰으로 이미지 조회
```http
GET /api/secure-images/{token}
```

**헤더**:
```http
User-Agent: Mozilla/5.0...
Accept: image/*
```

### 2. 세션 토큰 발급
```http
POST /api/secure-images/session-token
```

**응답**:
```json
{
  "sessionToken": "abc123def456",
  "expiresIn": 7200000
}
```

### 3. 특정 파일용 토큰 생성
```http
GET /api/secure-images/generate/{fileName}
Authorization: Bearer <JWT_TOKEN>
```

**응답**:
```json
{
  "fileName": "logo.png",
  "token": "xyz789abc123",
  "secureUrl": "/api/secure-images/xyz789abc123"
}
```

### 4. 이미지 토큰 매핑 조회
```http
GET /api/secure-images/mapping
Authorization: Bearer <JWT_TOKEN>
```

### 5. 모든 이미지 토큰 새로고침
```http
POST /api/secure-images/refresh
Authorization: Bearer <JWT_TOKEN>
```

---

## 📁 파일 첨부 API

### 1. 파일 업로드
```http
POST /api/kitms-attaches/upload
Authorization: Bearer <JWT_TOKEN>
Content-Type: multipart/form-data

file: [파일]
```

**응답**:
```json
{
  "statusCode": 200,
  "message": "파일 업로드 성공",
  "data": {
    "attachNo": 1,
    "originalFileName": "document.pdf",
    "storedFileName": "20250126_123456_document.pdf",
    "filePath": "/upload/20250126_123456_document.pdf",
    "fileSize": 1024000,
    "contentType": "application/pdf"
  }
}
```

### 2. 파일 다운로드
```http
GET /api/kitms-attaches/{attachNo}/download
Authorization: Bearer <JWT_TOKEN>
```

### 3. 이미지 파일 찾기
```http
GET /api/kitms-attaches/find-image/{fileName}
Authorization: Bearer <JWT_TOKEN>
```

---

## 🍽️ 메뉴 관리 API

### 1. 활성 메뉴 조회
```http
GET /api/kitms-menus/active
```

**응답**:
```json
{
  "statusCode": 200,
  "message": "활성 메뉴 조회 성공",
  "data": [
    {
      "menuId": 1,
      "menuName": "회사소개",
      "menuUrl": "/company",
      "parentId": null,
      "menuLevel": 1,
      "sortOrder": 1,
      "isActive": "Y"
    }
  ]
}
```

### 2. 관리자 메뉴 조회
```http
GET /api/admin-menus
Authorization: Bearer <JWT_TOKEN>
```

### 3. 관리자 메뉴 생성
```http
POST /api/admin-menus
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "menuName": "새 메뉴",
  "menuUrl": "/new-menu",
  "menuDescription": "메뉴 설명",
  "menuIcon": "icon-home",
  "menuOrder": 1,
  "isActive": true,
  "isVisible": true
}
```

---

## 🔍 공통 응답 형식

### 성공 응답
```json
{
  "statusCode": 200,
  "message": "요청 처리 성공",
  "data": { ... }
}
```

### 에러 응답
```json
{
  "statusCode": 400,
  "message": "요청 처리 실패",
  "error": "상세 에러 메시지"
}
```

### 페이지네이션 응답
```json
{
  "statusCode": 200,
  "message": "조회 성공",
  "data": {
    "content": [...],
    "totalElements": 100,
    "totalPages": 5,
    "size": 20,
    "number": 0,
    "first": true,
    "last": false
  }
}
```

---

## 🚨 에러 코드

| 코드 | 설명 |
|------|------|
| 200 | 성공 |
| 400 | 잘못된 요청 |
| 401 | 인증 실패 |
| 403 | 권한 없음 |
| 404 | 리소스 없음 |
| 410 | 리소스 만료 (이미지 토큰 만료) |
| 500 | 서버 내부 오류 |

---

## 📝 사용 예시

### JavaScript에서 API 호출
```javascript
// 토큰 발급
const loginResponse = await fetch('/api/kitms-login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    username: 'admin',
    password: 'admin123'
  })
});

const loginData = await loginResponse.json();
const token = loginData.data.idToken;

// 공지사항 목록 조회
const noticesResponse = await fetch('/api/kitms-notices', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

const noticesData = await noticesResponse.json();
console.log(noticesData.data.content);
```

### cURL 예시
```bash
# 로그인
curl -X POST http://localhost:8080/api/kitms-login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 공지사항 조회
curl -X GET http://localhost:8080/api/kitms-notices \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

**문서 버전**: 1.0  
**마지막 업데이트**: 2025년 1월 26일

