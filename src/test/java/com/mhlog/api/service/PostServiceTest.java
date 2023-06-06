package com.mhlog.api.service;

import com.mhlog.api.domain.Post;
import com.mhlog.api.repository.PostRepository;
import com.mhlog.api.request.WritePost;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void writePost() {
        // given
        WritePost writePost = WritePost.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(writePost);

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회X")
    void selectOnePost_X() {
        // given
        Long postId = 1L;

        // when
//        Post post = postService.get(postId);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> postService.get(postId));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("존재하지 않는 글입니다.");
    }

    @Test
    @DisplayName("글 1개 조회O")
    void selectOnePost_O() {
        // given
        Post requestPost = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(requestPost);

        // when
        Post post = postService.get(requestPost.getId());

        // then
        assertNotNull(post);
        assertEquals("제목", post.getTitle());
        assertEquals("내용", post.getContent());

    }
}