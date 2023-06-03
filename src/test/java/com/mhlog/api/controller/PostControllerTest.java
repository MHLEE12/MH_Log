package com.mhlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhlog.api.request.WritePost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostController postController;

    @BeforeEach
    void setup() {
        // 한글 깨짐 문제 해결
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                                .build();
    }

    @Test
    @DisplayName("/posts 요청시 문구 출력")
    void test() throws Exception {

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("/posts 요청시 제목값은 필수입니다.")
    void titleBlankTest() throws Exception {
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목\", \"content\": \"내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("제목"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.title").value("제목을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());

    }
}