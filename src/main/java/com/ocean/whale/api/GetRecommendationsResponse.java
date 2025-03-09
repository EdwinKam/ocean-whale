package com.ocean.whale.api;

import java.util.List;

import com.ocean.whale.model.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetRecommendationsResponse {
    List<Post> posts;
}
