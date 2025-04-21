package com.example.jwtauthdemo.service;

import com.example.jwtauthdemo.entity.Post;
import com.example.jwtauthdemo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post updatePost(Long id, Post post) {
        if (postRepository.existsById(id)) {
            post.setId(id);
            return postRepository.save(post);
        }
        return null;
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
