package com.paccy.TrendTeller.services;

import com.paccy.TrendTeller.dto.PostDTO;
import com.paccy.TrendTeller.dto.PostResponseDTO;
import com.paccy.TrendTeller.exceptions.NotFoundException;
import com.paccy.TrendTeller.models.Post;
import com.paccy.TrendTeller.repositories.PostRepository;
import com.paccy.TrendTeller.services.impl.PostServiceImpl;
import org.assertj.core.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {
    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void testCreatePostSucceeds() {
        PostDTO postDTO = PostDTO.builder()
                .title("Test Post")
                .description("Test Description")
                .content("Test Content")
                .build();
        Post post = Post.builder()
                .title("Test Post")
                .description("Test Description")
                .content("Test Content")
                .build();

        when(modelMapper.map(postDTO, Post.class)).thenReturn(post);
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);

        Post createdPost = postService.createPost(postDTO);

        Assertions.assertThat(createdPost).isNotNull();
        Assertions.assertThat(createdPost.getTitle()).isEqualTo(postDTO.getTitle());
        Assertions.assertThat(createdPost.getDescription()).isEqualTo(postDTO.getDescription());
        Assertions.assertThat(createdPost.getContent()).isEqualTo(postDTO.getContent());
    }

    @Test
    public void testGetAllPostsSucceeds() {
        PostResponseDTO postResponseDTO = Mockito.mock(PostResponseDTO.class);
        Page<Post> posts = Mockito.mock(Page.class);

        when(postRepository.findAll(Mockito.any(Pageable.class))).thenReturn(posts);

        PostResponseDTO response = postService.getAllPosts(0, 10, "id", "asc");

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void testGetPostByIdSucceeds() {
        Post post = Post.builder()
                .title("Test Post")
                .description("Test Description")
                .content("Test Content")
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post foundPost = postService.getPostById(1L);

        Assertions.assertThat(foundPost).isNotNull();
        Assertions.assertThat(foundPost.getTitle()).isEqualTo(post.getTitle());
        Assertions.assertThat(foundPost.getDescription()).isEqualTo(post.getDescription());
        Assertions.assertThat(foundPost.getContent()).isEqualTo(post.getContent());
    }
    
    @Test
    public void testGetPostByIdWithNonExistentIdFails() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> postService.getPostById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Post")
                .hasMessageContaining("id")
                .hasMessageContaining("1");
    }

    @Test
    public void testUpdatePostSucceeds() {
        PostDTO postDTO = PostDTO.builder()
                .title("Test Post")
                .description("Test Description")
                .content("Test Content")
                .build();
        Post post = Post.builder()
                .title("Test Post")
                .description("Test Description")
                .content("Test Content")
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);

        Post updatedPost = postService.updatePost(1L, postDTO);

        Assertions.assertThat(updatedPost).isNotNull();
        Assertions.assertThat(updatedPost.getTitle()).isEqualTo(postDTO.getTitle());
        Assertions.assertThat(updatedPost.getDescription()).isEqualTo(postDTO.getDescription());
        Assertions.assertThat(updatedPost.getContent()).isEqualTo(postDTO.getContent());
    }

    @Test
    public void testDeletePostSucceeds() {
        Post post = Post.builder()
                .title("Test Post")
                .description("Test Description")
                .content("Test Content")
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        assertAll(() -> postService.deletePost(1L));
    }
}
