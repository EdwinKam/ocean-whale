package com.ocean.whale.api;

import com.ocean.whale.model.PostRecommendationList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRecommendationsResponse {
    PostRecommendationList recommendedPostIds;
}
