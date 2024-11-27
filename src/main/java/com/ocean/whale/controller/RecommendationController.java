package com.ocean.whale.controller;

import com.ocean.whale.model.GetRecommendationsResponse;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.recommendation.RecommendationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    private final AuthService authService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService, AuthService authService) {
        this.recommendationService = recommendationService;
        this.authService = authService;
    }

    @GetMapping("/get")
    public GetRecommendationsResponse getRecommendations(@RequestHeader String accessToken) {
        // Verify the token and fetch the user UID
        String uid = authService.verifyAndFetchUid(accessToken);
        // Get recommendations based on the UID
        return new GetRecommendationsResponse(recommendationService.getRecommendations(uid));
    }
}
