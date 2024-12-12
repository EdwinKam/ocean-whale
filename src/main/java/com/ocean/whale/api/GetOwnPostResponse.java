package com.ocean.whale.api;

import com.ocean.whale.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GetOwnPostResponse {
    List<Post> posts;
}
