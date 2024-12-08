package com.ocean.whale.model;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PostRecommendationList {
    List<String> recommendations; // list of postId

    public static PostRecommendationList fromMap(Map<String, Object> map) {
        // Extract the recommendations list from the map
        @SuppressWarnings("unchecked")
        List<String> recommendations = (List<String>) map.get("recommendations");

        // Create and return a new instance of PostRecommendationList
        return new PostRecommendationList(recommendations);
    }
}
