package com.mhlog.api.controller;

import com.mhlog.api.domain.Post;
import com.mhlog.api.request.WritePost;
import com.mhlog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Post post(@RequestBody @Valid WritePost request) {

        return postService.write(request);
    }

    /**
     * 글 한개 조회
     */
    @GetMapping("/post/{postId}")
    public Post get(@PathVariable Long postId) {

        Post post = postService.get(postId);

        return post;
    }

}
