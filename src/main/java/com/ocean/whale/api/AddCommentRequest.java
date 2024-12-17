package com.ocean.whale.api;

import lombok.Getter;

@Getter
public class AddCommentRequest {
    String postId;
    String content;
    String parentCommentId;
}
