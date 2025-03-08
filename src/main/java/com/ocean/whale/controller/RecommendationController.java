package com.ocean.whale.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocean.whale.model.Post;
import com.ocean.whale.model.PostRecommendationList;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.post.PostService;
import com.ocean.whale.service.recommendation.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final PostService postService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService,
            AuthService authService, PostService postService) {
        this.recommendationService = recommendationService;
        this.postService = postService;
    }

    @PostMapping("/addRecommensationsForTesting")
    public void addRecommendationsForTesting(@RequestParam String uid) {
        List<Post> post = postService.getAllPosts();
        // Get recommendations based on the UID
        recommendationService.addRecommendations(uid,
                new PostRecommendationList(post.stream().map(Post::getId).toList()));
    }
}
