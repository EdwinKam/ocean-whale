package com.ocean.whale.api;

import com.ocean.whale.model.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetBatchPostResponse {
    List<Post> posts;
}
