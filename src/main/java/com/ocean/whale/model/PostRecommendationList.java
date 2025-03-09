package com.ocean.whale.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("ALL")
@AllArgsConstructor
@Getter
public class PostRecommendationList {
    List<String> recommendations; // list of postId

    public static PostRecommendationList fromMap(Map<String, Object> map) {
        // Extract the recommendations list from the map
        List<String> recommendations = new ArrayList<>();

        try {
            if (map.containsKey("recommendedPosts")
                    && map.get("recommendedPosts") instanceof List) {
                recommendations = (List<String>) map.get("recommendedPosts");
            }
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.BAD_DATA_ERROR,
                    "recommendations not a string list");
        }

        // Create and return a new instance of PostRecommendationList
        return new PostRecommendationList(recommendations);
    }
}
