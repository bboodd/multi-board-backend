-- src/test/resources/sql/member-test-data.sql

-- 기존 테스트 데이터 삭제
DELETE FROM comments;
DELETE FROM files;
DELETE FROM posts;
DELETE FROM members;
DELETE FROM categories;
DELETE FROM board_types;
DELETE FROM roles;

-- 역할 데이터 추가
INSERT INTO roles (role_id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

-- 테스트용 회원 데이터 추가
INSERT INTO members (member_id, role_id, login_id, password, nickname)
VALUES (1, 1, 'testUser', 'encodedPassword', '테스트유저'),  -- 일반 사용자
       (2, 2, 'admin', 'encodedPassword', '관리자');         -- 관리자