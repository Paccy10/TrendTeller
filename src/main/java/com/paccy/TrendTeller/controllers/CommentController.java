package com.paccy.TrendTeller.controllers;

import com.paccy.TrendTeller.dto.CommentDTO;
import com.paccy.TrendTeller.dto.CommentResponseDTO;
import com.paccy.TrendTeller.models.Comment;
import com.paccy.TrendTeller.services.CommentService;
import com.paccy.TrendTeller.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@PathVariable Long postId, @Valid @RequestBody CommentDTO commentDTO) {
        Comment comment = commentService.createComment(postId, commentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping
    public ResponseEntity<CommentResponseDTO> getCommentsByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_LIMIT, required = false) int limit,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_ORDER, required = false) String sortOrder
    ) {
        CommentResponseDTO commentResponseDTO = commentService.getCommentsByPostId(postId, page, limit, sortBy, sortOrder);

        return ResponseEntity.ok(commentResponseDTO);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long postId, @PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(postId, commentId);

        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long postId,
                                                 @PathVariable Long commentId,
                                                 @Valid @RequestBody CommentDTO commentDTO) {
        Comment comment = commentService.updateComment(postId, commentId, commentDTO);

        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(postId, commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
