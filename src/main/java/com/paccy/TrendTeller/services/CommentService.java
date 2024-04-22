package com.paccy.TrendTeller.services;

import com.paccy.TrendTeller.dto.CommentDTO;
import com.paccy.TrendTeller.dto.CommentResponseDTO;
import com.paccy.TrendTeller.models.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long postId, CommentDTO commentDTO);
    CommentResponseDTO getCommentsByPostId(Long postId, int page, int limit, String sortBy, String sortOrder);
    Comment getCommentById(Long postId, Long commentId);
    Comment updateComment(Long postId, Long commentId, CommentDTO commentDTO);
    void deleteComment(Long postId, Long commentId);
}
