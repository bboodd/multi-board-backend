
-- 기존 데이터 삭제
DELETE FROM comments;
DELETE FROM files;
DELETE FROM posts;
DELETE FROM categories;
DELETE FROM board_types;
DELETE FROM members;
DELETE FROM roles;

-- roles 데이터 추가
INSERT INTO roles (role_id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

-- members 데이터 추가
INSERT INTO members (
    member_id,
    role_id,
    login_id,
    password,
    nickname,
    is_deleted,
    created_at
) VALUES
    (1, 1, 'testUser', 'password', '테스트유저', false, CURRENT_TIMESTAMP);

-- board_types 데이터 추가
INSERT INTO board_types (
    board_type_id,
    name,
    code,
    is_deleted,
    created_at
) VALUES
      (1, '자유게시판', 'FREE', false, CURRENT_TIMESTAMP),
      (2, '갤러리', 'GALLERY', false, CURRENT_TIMESTAMP),
      (3, '공지게시판', 'NOTICE', false, CURRENT_TIMESTAMP);

-- categories 데이터 추가
INSERT INTO categories (
    category_id,
    board_type_id,
    name,
    is_deleted,
    created_at
) VALUES
      (1, 1, '일반', false, CURRENT_TIMESTAMP),
      (2, 1, '질문', false, CURRENT_TIMESTAMP);

-- posts 데이터 추가
INSERT INTO posts (
    post_id,
    board_type_id,
    member_id,
    category_id,
    title,
    content,
    view_count,
    is_deleted,
    created_at
) VALUES
      (1, 1, 1, 1, '테스트 게시글 1', '내용 1', 0, false, CURRENT_TIMESTAMP),
      (2, 1, 1, 1, '테스트 게시글 2', '내용 2', 0, false, CURRENT_TIMESTAMP);

-- files 데이터 추가
INSERT INTO files (
    file_id,
    post_id,
    original_name,
    saved_name,
    saved_path,
    file_size,
    content_type
) VALUES
    (1, 1, 'test1.txt', 'saved1.txt', '/files', 1024, 'text/plain');

-- comments 데이터 추가
INSERT INTO comments (
    comment_id,
    post_id,
    member_id,
    content,
    is_deleted,
    created_at
) VALUES
    (1, 1, 1, '테스트 댓글 1', false, CURRENT_TIMESTAMP);