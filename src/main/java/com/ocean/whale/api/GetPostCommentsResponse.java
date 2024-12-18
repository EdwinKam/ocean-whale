package com.ocean.whale.api;

import com.ocean.whale.model.PostComment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetPostCommentsResponse {
    List<PostComment> postComments;
}
