# 게시판 프로젝트 - 백엔드

게시판 프로젝트의 백엔드 서버 입니다. API 요청 처리와 관리자 페이지를 보여줍니다.

<br>

---
## 📌 1. 사용 기술 스택

### Backend
- Java 17
- Spring Boot 3.2.1, Spring Security 6
- MyBatis 3.5
- Junit5, Mockito
- Gradle 8.8
- IntelliJ
- MySQL 8.0
- AWS EC2, RDS, S3
- Docker
- Nginx

### Frontend
- Thymeleaf
- JavaScript
- HTML
- CSS
- Bootstrap

---
## 📌 2. 기능

### 2-1. 구현
- 게시글, 카테고리, 댓글 CRUD API 구현
  - JWT 인증 적용
  - 게시글 첨부파일, 썸네일 저장 s3 적용
  - 파일 다운로드 구현
  - 게시글 목록 조회 페이징 적용
  - 테스트 코드 작성
- 로그인/회원가입 API 구현
  - 테스트 코드 작성
- 공통 에러 핸들링, 커스텀 예외 구현
- admin 페이지 구현
  - session 인증 적용
  - bootstrap 적용
  - thymeleaf layout 적용

### 2-1. API
- API 문서 : https://api.bboodd-board.site/swagger-ui/index.html#/
  <br>
  <img width="1472" alt="api 1" src="https://github.com/user-attachments/assets/4abd28f0-9ed7-4c8d-a44f-eb0ed71a1951" />

  <img width="1467" alt="api 2" src="https://github.com/user-attachments/assets/45c3b302-ca8a-46f0-98cb-5520f9eaa470" />


### 2-2. 관리자 페이지
|    기능    |              설명              |                               비고                               |
|:--------:|:----------------------------:|:--------------------------------------------------------------:|
|   로그인   |       관리자 권한 로그인,로그아웃       |    Session 방식 로그인, 로그아웃    |
|   게시글 등록   |       모든 게시판 게시글 등록       |    모든 게시판의 게시글 등록 가능     |
|   게시글 읽기   |       모든 게시글 읽기       |    비밀글, 문의글 읽기 가능     |
|   게시글 수정   |       모든 게시글 수정       |    모든 게시글 수정 가능     |
|   게시글 삭제   |       모든 게시글 삭제       |    모든 게시글 삭제 가능     |
|   댓글 등록    |       모든 게시판 댓글 등록       |    모든 게시글 댓글 등록, 문의 게시글의 답변 등록     |
|   댓글 삭제   |       모든 게시판 댓글 삭제       |    모든 댓글 삭제 가능     |
|   파일 다운로드   |       파일 다운로드       |    파일 다운로드 가능     |

---
## 📌 3. ERD

- ERD 링크 : https://www.erdcloud.com/d/53KjJ8pm7RnSqyRcw

<img width="1192" alt="erd" src="https://github.com/user-attachments/assets/0701f96a-1cb5-4704-80d3-e1134877a228" />

---
## 📌 4. 아키텍처


![board-아키텍처2 drawio](https://github.com/user-attachments/assets/ab321234-a931-4247-9edf-9e65fc7c2c94)


---
## 📌 5. 관리자 페이지 화면
링크: https://api.bboodd-board.site/admin/login

테스트 ID: admin

테스트 PW: admin

✔ 로그인/로그아웃
- 세션 로그인 (권한이 ROLE_ADMIN인 유저만 로그인 가능)

✔ 대시보드
- 각 게시판의 글을 5개씩 보여줌
- 더보기를 누를 시 각 게시판으로 이동

✔ 게시판 메인
- 검색 조건 별 페이징

✔ 게시글 등록/수정
- 등록시 파일첨부, 수정시 댓글 등록/수정 가능
  

