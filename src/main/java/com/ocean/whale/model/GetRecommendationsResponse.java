package com.ocean.whale.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetRecommendationsResponse {
    List<String> recommendedPostIds;
}
