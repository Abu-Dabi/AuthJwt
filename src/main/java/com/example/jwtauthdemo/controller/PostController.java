package com.example.jwtauthdemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.jwtauthdemo.entity.Post;
import com.example.jwtauthdemo.service.PostService;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // Create - Требуется авторизация
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // Read - Требуется авторизация
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        Post post = postService.getPost(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // Update - Требуется авторизация
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        Post updatedPost = postService.updatePost(id, post);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // Delete - Требуется авторизация
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

