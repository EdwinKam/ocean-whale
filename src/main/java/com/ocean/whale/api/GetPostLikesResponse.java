package com.ocean.whale.api;

import java.util.List;

import com.ocean.whale.model.PostLike;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPostLikesResponse {
    List<PostLike> postLikes;
}
