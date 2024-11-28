package com.ocean.whale.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PostViewHistory {
    String postId;
    String userId;
    boolean isPreviewed;
    boolean isRead;
}
