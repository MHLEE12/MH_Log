package com.mhlog.api.service;

import com.mhlog.api.domain.Post;
import com.mhlog.api.repository.PostRepository;
import com.mhlog.api.request.WritePost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
