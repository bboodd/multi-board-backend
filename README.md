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
- jQuery
- JavaScript
- HTML
- CSS
- Bootstrap

---
## 📌 2. 기능

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
- 로그인
  

