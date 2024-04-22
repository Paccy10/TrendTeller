package com.paccy.TrendTeller.repositories;

import com.paccy.TrendTeller.models.Comment;
import com.paccy.TrendTeller.models.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentRepositoryTests {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;
    private Post post;

    @BeforeEach
    public void savePost() {
        Post post = Post.builder()
                .title("Test Post")
                .description("Test Description")
                .content("Test Content")
                .build();
        this.post = postRepository.save(post);

    }

    @Test
    public void testSaveComment() {
        Comment comment = Comment.builder()
                .name("Test Comment")
                .email("test@app.com")
                .body("Test Body")
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(comment);

        Assertions.assertThat(savedComment).isNotNull();
        Assertions.assertThat(savedComment.getId()).isGreaterThan(0);
        Assertions.assertThat(savedComment.getPost()).isEqualTo(post);
    }

    @Test
    public void testFindByPostId() {
        Comment comment1 = Comment.builder()
                .name("Tester 1")
                .email("test1@app.com")
                .body("Test Body 1")
                .post(post)
                .build();
        Comment comment2 = Comment.builder()
                .name("Tester 2")
                .email("test2@app.com")
                .body("Test Body 2")
                .post(post)
                .build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findByPostId(post.getId(), null).getContent();

        Assertions.assertThat(comments).isNotNull();
        Assertions.assertThat(comments.size()).isEqualTo(2);
    }

    @Test
    public void testFindCommentById() {
        Comment comment = Comment.builder()
                .name("Test Comment")
                .email("test@app.com")
                .body("Test Body")
                .post(post)
                .build();
        commentRepository.save(comment);

        Comment foundComment = commentRepository.findById(comment.getId()).orElse(null);

        Assertions.assertThat(foundComment).isNotNull();
        Assertions.assertThat(foundComment.getId()).isEqualTo(comment.getId());
        Assertions.assertThat(foundComment.getName()).isEqualTo(comment.getName());
    }

    @Test
    public void testDeleteComment() {
        Comment comment = Comment.builder()
                .name("Test Comment")
                .email("test@app.com")
                .body("Test Body")
                .post(post)
                .build();
        commentRepository.save(comment);
        commentRepository.delete(comment);

        Comment deletedComment = commentRepository.findById(comment.getId()).orElse(null);

        Assertions.assertThat(deletedComment).isNull();
    }
}
