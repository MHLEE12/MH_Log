package com.mhlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhlog.api.domain.Post;
import com.mhlog.api.repository.PostRepository;
import com.mhlog.api.request.WritePost;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostController postController;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // 한글 깨짐 문제 해결
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                                .build();
    }

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 문구 출력")
    void test() throws Exception {
        // given
        WritePost request = WritePost.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());

    }

//    @Test
//    @DisplayName("/posts 요청시 제목값은 필수입니다.")
//    void titleBlankTest() throws Exception {
//
//        // given
//        WritePost request = WritePost.builder()
//                .content("내용입니다.")
//                .build();
//
//        String json = objectMapper.writeValueAsString(request);
//
//        // expected
//        mockMvc.perform(post("/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
////                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
////                .andExpect(jsonPath("$.validation.title").value("제목을 입력해주세요."))
//                .andDo(MockMvcResultHandlers.print());
//
//    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void postSaveTest() throws Exception {

        // given
        WritePost request = WritePost.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void selectOnePost() throws Exception {
        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("내용")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/post/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void selectManyPosts() throws Exception {
        // given
        Post post1 = postRepository.save(Post.builder()
                .title("제목1")
                .content("내용1")
                .build());

        Post post2 = postRepository.save(Post.builder()
                .title("제목2")
                .content("내용2")
                .build());

        // expected
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(2)))
                .andExpect(jsonPath("$.[0].id").value(post1.getId()))
                .andExpect(jsonPath("$.[0].title").value("제목1"))
                .andExpect(jsonPath("$.[0].content").value("내용1"))
                .andExpect(jsonPath("$.[1].id").value(post2.getId()))
                .andExpect(jsonPath("$.[1].title").value("제목2"))
                .andExpect(jsonPath("$.[1].content").value("내용2"))
                .andDo(print());
    }



}