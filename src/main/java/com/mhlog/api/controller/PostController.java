package com.mhlog.api.controller;

import com.mhlog.api.domain.Post;
import com.mhlog.api.exception.InvalidRequest;
import com.mhlog.api.request.PostEdit;
import com.mhlog.api.request.PostSearch;
import com.mhlog.api.request.WritePost;
import com.mhlog.api.response.PostResponse;
import com.mhlog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Post post(@RequestBody @Valid WritePost request) {
        request.validate();

        return postService.write(request);
    }

    /**
     * 글 한개 조회
     */
    @GetMapping("/post/{postId}")
    public PostResponse get(@PathVariable Long postId) {

        PostResponse response = postService.get(postId);

        return response;
    }

    /**
     * 여러개 글 조회
     */
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {

        return postService.getList(postSearch);
    }

    /**
     * 글 수정
     */
    @PatchMapping("/post/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.updatePost(postId, request);
    }

    /**
     * 글 삭제
     */
    @DeleteMapping("/post/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
