package com.paccy.TrendTeller.services;

import com.paccy.TrendTeller.dto.PostDTO;
import com.paccy.TrendTeller.dto.PostResponseDTO;
import com.paccy.TrendTeller.models.Post;

import java.util.List;

public interface PostService {
    Post createPost(PostDTO postDTO);
    PostResponseDTO getAllPosts(int page, int limit, String sortBy, String sortOrder);
    Post getPostById(Long id);
    Post updatePost(Long id, PostDTO postDTO);
    void deletePost(Long id);
}
