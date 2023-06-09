package com.mhlog.api.service;

import com.mhlog.api.domain.Post;
import com.mhlog.api.repository.PostRepository;
import com.mhlog.api.request.PostSearch;
import com.mhlog.api.request.WritePost;
import com.mhlog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post write(WritePost writePost) {
        Post post = Post.builder()
                        .title(writePost.getTitle())
                        .content(writePost.getContent())
                        .build();
        return postRepository.save(post);
    }

    public PostResponse get(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return postResponse;
    }

    public List<PostResponse> getList(PostSearch postSearch) {

//        Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());

        return postRepository.getList(postSearch).stream()
                .map(post -> new PostResponse(post))
                .collect(Collectors.toList());
    }
}
