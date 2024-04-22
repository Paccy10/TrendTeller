package com.paccy.TrendTeller.services;

import com.paccy.TrendTeller.dto.CommentDTO;
import com.paccy.TrendTeller.dto.CommentResponseDTO;
import com.paccy.TrendTeller.models.Comment;
import com.paccy.TrendTeller.models.Post;
import com.paccy.TrendTeller.repositories.CommentRepository;
import com.paccy.TrendTeller.repositories.PostRepository;
import com.paccy.TrendTeller.services.impl.CommentServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {
    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    public void init() {
        Post post = Post.builder()
                .id(1L)
                .title("Test Post")
                .description("Test Description")
                .content("Test Content")
                .build();
        when(postService.getPostById(1L)).thenReturn(post);
    }

    @Test
    public void testCreateCommentSucceeds() {
        CommentDTO commentDTO = CommentDTO.builder()
                .name("Tester")
                .email("tester@app.com")
                .body("Test Body")
                .build();
        Comment comment = Comment.builder()
                .name("Tester")
                .email("tester@app.com")
                .body("Test Body")
                .build();

        when(modelMapper.map(commentDTO, Comment.class)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment createdComment = commentService.createComment(1L, commentDTO);

        Assertions.assertThat(createdComment).isNotNull();
        Assertions.assertThat(createdComment.getName()).isEqualTo(commentDTO.getName());
        Assertions.assertThat(createdComment.getEmail()).isEqualTo(commentDTO.getEmail());
        Assertions.assertThat(createdComment.getBody()).isEqualTo(commentDTO.getBody());
    }

    @Test
    public void testGetCommentsByPostIdSucceeds() {
        CommentResponseDTO commentResponseDTO = Mockito.mock(CommentResponseDTO.class);
        Page<Comment> comments = Mockito.mock(Page.class);

        when(commentRepository.findByPostId(Mockito.eq(1L), Mockito.any(Pageable.class))).thenReturn(comments);

        CommentResponseDTO response = commentService.getCommentsByPostId(1L, 0, 10, "id", "asc");

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void testGetCommentByIdSucceeds() {
        Comment comment = Comment.builder()
                .name("Tester")
                .email("tester@app.com")
                .body("Test Body")
                .post(Post.builder().id(1L).title("Post title").build())
                .build();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.getCommentById(1L, 1L);

        Assertions.assertThat(foundComment).isNotNull();
        Assertions.assertThat(foundComment.getId()).isEqualTo(comment.getId());

    }
}
