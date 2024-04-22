package com.paccy.TrendTeller.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paccy.TrendTeller.dto.PostDTO;
import com.paccy.TrendTeller.dto.PostResponseDTO;
import com.paccy.TrendTeller.exceptions.NotFoundException;
import com.paccy.TrendTeller.models.Post;
import com.paccy.TrendTeller.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PostControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    private PostDTO postDTO;
    private Post post;

    @BeforeEach
    public void setUp() {
            postDTO = PostDTO.builder()
                    .title("Test Post")
                    .description("Test Description")
                    .content("Test Content")
                    .build();
            post = Post.builder()
                    .id(1L)
                    .title("Test Post")
                    .description("Test Description")
                    .content("Test Content")
                    .build();
        }


    @Test
    public void testCreatePost() throws Exception {
        when(postService.createPost(postDTO)).thenReturn(post);

        ResultActions response = mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDTO)));

        response.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(post.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(post.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(post.getContent()));
    }

    @Test
    public void testCreatePostWithInvalidData() throws Exception {
        postDTO.setTitle(null);
        postDTO.setDescription(null);
        postDTO.setContent(null);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("must not be empty"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("must not be empty"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("must not be empty"));
    }

    @Test
    public void testGetAllPosts() throws Exception {
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setData(Collections.singletonList(post));
        postResponseDTO.setPage(0);
        postResponseDTO.setLimit(10);
        postResponseDTO.setTotal(1);
        postResponseDTO.setPages(1);

        given(postService.getAllPosts(0, 10, "id", "asc")).willReturn(postResponseDTO);

        mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").value(postResponseDTO.getPage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.limit").value(postResponseDTO.getLimit()));
    }

    @Test
    public void testGetPostById() throws Exception {
        given(postService.getPostById(1L)).willReturn(post);

        mockMvc.perform(get("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(post.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(post.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(post.getContent()));
    }

    @Test
    public void testGetPostByIdWithInvalidId() throws Exception {
        given(postService.getPostById(1L)).willThrow(new NotFoundException("Post", "id", 1L));

        mockMvc.perform(get("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Post with id '1' not found"));
    }

    @Test
    public void testUpdatePost() throws Exception {

        when(postService.updatePost(1L, postDTO)).thenReturn(post);

        mockMvc.perform(put("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(post.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(post.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(post.getContent()));
    }

    @Test
    public void testDeletePost() throws Exception {
        mockMvc.perform(delete("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
