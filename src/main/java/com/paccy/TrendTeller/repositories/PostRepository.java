package com.paccy.TrendTeller.repositories;

import com.paccy.TrendTeller.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByTitle(String title);
    Boolean existsByTitle(String title);
}