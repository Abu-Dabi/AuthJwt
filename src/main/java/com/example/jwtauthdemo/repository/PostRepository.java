package com.example.jwtauthdemo.repository;

import com.example.jwtauthdemo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
