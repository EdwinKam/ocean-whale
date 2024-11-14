package com.ocean.whale.controller;

import com.ocean.whale.model.Post;
import com.ocean.whale.service.FirestoreService;
import com.ocean.whale.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/createPost")
    public String createPost(@RequestParam String content, @RequestParam Integer userId) {
        // curl -X POST "http://localhost:8080/api/createPost" -d "content=Hello" -d "userId=123"
        try {
            postService.createPost(new Post(1, content, userId));
            return "success creating new post";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/getPosts")
    public List<Map<String, Object>> getPosts() throws Exception {
        return postService.getAllPosts();
    }


}
