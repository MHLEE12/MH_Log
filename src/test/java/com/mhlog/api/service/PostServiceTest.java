package com.mhlog.api.service;

import com.mhlog.api.domain.Post;
import com.mhlog.api.repository.PostRepository;
import com.mhlog.api.request.PostEdit;
import com.mhlog.api.request.PostSearch;
import com.mhlog.api.request.WritePost;
import com.mhlog.api.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals("제목", response.getTitle());
        assertEquals("내용", response.getContent());

    }

    @Test
    @DisplayName("글 1페이지 조회")
    void selectPosts() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("제목 - " + i)
                        .content("내용 - " + i)
                        .build())
                        .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10, posts.size());
        assertEquals("제목 - 30", posts.get(0).getTitle());

    }

    @Test
    @DisplayName("글 제목 수정")
    void updatePostTitle() {
        // given
        Post post = Post.builder()
                        .title("제목")
                        .content("내용")
                        .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                                .title("제목 수정")
                                .content("내용")
                                .build();

        // when
        postService.updatePost(post.getId(), postEdit);

        // then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id= " + post.getId()));

        assertEquals("제목 수정", changePost.getTitle());
    }

    @Test
    @DisplayName("글 내용 수정")
    void updatePostContent() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("내용 수정")
                .build();

        // when
        postService.updatePost(post.getId(), postEdit);

        // then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id= " + post.getId()));

        assertEquals("제목", changePost.getTitle());
        assertEquals("내용 수정", changePost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }

}