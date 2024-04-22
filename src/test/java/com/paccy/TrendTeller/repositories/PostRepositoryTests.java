package com.paccy.TrendTeller.repositories;

import com.paccy.TrendTeller.models.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PostRepositoryTests {
    @Autowired
    private PostRepository postRepository;

    @Test
    public void testSavePost() {
        Post post = Post.builder()
                        .title("Test Post")
                        .description("Test Description")
                        .content("Test Content")
                        .build();
        Post savedPost = postRepository.save(post);

        Assertions.assertThat(savedPost).isNotNull();
        Assertions.assertThat(savedPost.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAllPosts() {
        Post post1 = Post.builder()
                         .title("Test Post 1")
                         .description("Test Description 1")
                         .content("Test Content 1")
                         .build();
        Post post2 = Post.builder()
                         .title("Test Post 2")
                         .description("Test Description 2")
                         .content("Test Content 2")
                         .build();
        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> posts = postRepository.findAll();

        Assertions.assertThat(posts).isNotNull();
        Assertions.assertThat(posts.size()).isEqualTo(2);
    }

    @Test
    public void testFindPostById() {
        Post post = Post.builder()
                        .title("Test Post")
                        .description("Test Description")
                        .content("Test Content")
                        .build();
        postRepository.save(post);

        Post foundPost = postRepository.findById(post.getId()).orElse(null);

        Assertions.assertThat(foundPost).isNotNull();
        Assertions.assertThat(foundPost.getId()).isEqualTo(post.getId());
    }

    @Test
    public void testDeletePost() {
        Post post = Post.builder()
                        .title("Test Post")
                        .description("Test Description")
                        .content("Test Content")
                        .build();
        postRepository.save(post);
        postRepository.delete(post);

        Post foundPost = postRepository.findById(post.getId()).orElse(null);

        Assertions.assertThat(foundPost).isNull();
    }
}
