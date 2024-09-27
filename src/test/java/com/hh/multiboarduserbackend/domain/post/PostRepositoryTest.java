package com.hh.multiboarduserbackend.domain.post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;


    @Test
    void 게시글_저장_성공() {
        //given
        PostVo post = PostVo.builder()
                .memberId(1L)
                .categoryId(1L)
                .title("제목")
                .content("내용")
                .typeId(1L)
                .build();

        //when
        postRepository.save(post);

        //then
        assertTrue(Optional.ofNullable(post.getPostId()).isPresent());
    }

}