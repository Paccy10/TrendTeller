package com.paccy.TrendTeller.services.impl;

import com.paccy.TrendTeller.dto.PostDTO;
import com.paccy.TrendTeller.dto.PostResponseDTO;
import com.paccy.TrendTeller.exceptions.BlogAPIException;
import com.paccy.TrendTeller.exceptions.NotFoundException;
import com.paccy.TrendTeller.models.Post;
import com.paccy.TrendTeller.repositories.PostRepository;
import com.paccy.TrendTeller.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Post createPost(PostDTO postDTO) {
        if(postRepository.existsByTitle(postDTO.getTitle())) {
            throw new BlogAPIException("Post with title '" + postDTO.getTitle() + "' already exists", HttpStatus.CONFLICT);
        }

        Post post = modelMapper.map(postDTO, Post.class);

        return postRepository.save(post);
    }

    @Override
    public PostResponseDTO getAllPosts(int page, int limit, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setData(posts.getContent());
        postResponseDTO.setPage(posts.getNumber());
        postResponseDTO.setLimit(posts.getSize());
        postResponseDTO.setTotal(posts.getTotalElements());
        postResponseDTO.setPages(posts.getTotalPages());

        return postResponseDTO;
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post", "id", id));
    }

    @Override
    public Post updatePost(Long id, PostDTO postDTO) {
        Post post  = getPostById(id);
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());

        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }
}
