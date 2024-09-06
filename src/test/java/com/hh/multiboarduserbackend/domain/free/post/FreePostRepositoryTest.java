package com.hh.multiboarduserbackend.domain.free.post;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FreePostRepositoryTest {

    @Autowired
    FreePostRepository freePostRepository;

    @Test
    void 게시글_저장_성공() {
        //given
        FreePostVo post = FreePostVo.builder()
                .memberId(1L)
                .freeCategoryId(1L)
                .title("제목")
                .content("내용")
                .build();

        //when
        freePostRepository.save(post);

        //then
        assertTrue(Optional.ofNullable(post.getFreePostId()).isPresent());
    }

}