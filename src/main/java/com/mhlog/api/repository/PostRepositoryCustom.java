package com.mhlog.api.repository;

import com.mhlog.api.domain.Post;
import com.mhlog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
