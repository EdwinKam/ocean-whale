package com.ocean.whale.controller;

import com.ocean.whale.api.GetRecommendationsResponse;
import com.ocean.whale.model.Post;
import com.ocean.whale.model.PostRecommendationList;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.post.PostService;
import com.ocean.whale.service.recommendation.RecommendationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    private final AuthService authService;
    private final PostService postService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService, AuthService authService, PostService postService) {
        this.recommendationService = recommendationService;
        this.authService = authService;
        this.postService = postService;
    }

    @GetMapping("/get")
    public GetRecommendationsResponse getRecommendations(@RequestHeader String accessToken) {
        // Verify the token and fetch the user UID
        String uid = authService.verifyAndFetchUid(accessToken);
        // Get recommendations based on the UID
        return new GetRecommendationsResponse(recommendationService.getRecommendations(uid));
    }

    @PostMapping("/addRecommensationsForTesting")
    public void addRecommendationsForTesting(@RequestParam String uid) {
        List<Post> post = postService.getAllPosts();
        // Get recommendations based on the UID
        recommendationService.addRecommendations(uid, new PostRecommendationList(post.stream().map(Post::getId).toList()));
    }
}
