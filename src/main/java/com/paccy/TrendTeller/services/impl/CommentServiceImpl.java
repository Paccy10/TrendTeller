package com.paccy.TrendTeller.services.impl;

import com.paccy.TrendTeller.dto.CommentDTO;
import com.paccy.TrendTeller.dto.CommentResponseDTO;
import com.paccy.TrendTeller.exceptions.BlogAPIException;
import com.paccy.TrendTeller.exceptions.NotFoundException;
import com.paccy.TrendTeller.models.Comment;
import com.paccy.TrendTeller.models.Post;
import com.paccy.TrendTeller.repositories.CommentRepository;
import com.paccy.TrendTeller.services.CommentService;
import com.paccy.TrendTeller.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostService postService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Comment createComment(Long postId, CommentDTO commentDTO) {
        Post post = postService.getPostById(postId);
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPost(post);

        return commentRepository.save(comment);
    }

    @Override
    public CommentResponseDTO getCommentsByPostId(Long postId, int page, int limit, String sortBy, String sortOrder) {
        Post post = postService.getPostById(postId);
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Comment> comments = commentRepository.findByPostId(post.getId(), pageable);

        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setData(comments.getContent());
        commentResponseDTO.setPage(comments.getNumber());
        commentResponseDTO.setLimit(comments.getSize());
        commentResponseDTO.setTotal(comments.getTotalElements());
        commentResponseDTO.setPages(comments.getTotalPages());

        return commentResponseDTO;
    }

    @Override
    public Comment getCommentById(Long postId, Long commentId) {
        Post post = postService.getPostById(postId);
        Comment comment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new NotFoundException("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(String.format("Comment with id '%s' does not belong to post with id '%s'", commentId, postId),
                                        HttpStatus.BAD_REQUEST);
        }

        return comment;
    }

    @Override
    public Comment updateComment(Long postId, Long commentId, CommentDTO commentDTO) {
        Comment comment = getCommentById(postId, commentId);
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());

        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = getCommentById(postId, commentId);
        commentRepository.delete(comment);
    }
}
