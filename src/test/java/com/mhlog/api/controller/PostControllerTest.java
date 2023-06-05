package com.mhlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhlog.api.domain.Post;
import com.mhlog.api.repository.PostRepository;
import com.mhlog.api.request.WritePost;
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

        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(MockMvcResultHandlers.print());

    }

//    @Test
//    @DisplayName("/posts 요청시 제목값은 필수입니다.")
//    void titleBlankTest() throws Exception {
//        // expected
//        mockMvc.perform(post("/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"title\": \"제목\", \"content\": \"내용입니다.\"}")
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("제목"))
////                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
////                .andExpect(jsonPath("$.validation.title").value("제목을 입력해주세요."))
//                .andDo(MockMvcResultHandlers.print());
//
//    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void postSaveTest() throws Exception {
        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }
}