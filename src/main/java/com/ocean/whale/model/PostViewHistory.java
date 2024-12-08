package com.ocean.whale.model;

import com.ocean.whale.util.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PostViewHistory {
    String postId;
    String userId;
    String viewType;
    long timestamp;
    String postViewHistoryId;

    public PostViewHistory(String postId, String userId, String viewType, long timestamp) {
        this.postId = postId;
        this.userId = userId;
        this.viewType = viewType;
        this.timestamp = timestamp;
        this.postViewHistoryId = IdGenerator.length(10);
    }

    public static PostViewHistory readHistory(String postId, String userId) {
        return new PostViewHistory(postId, userId, "READ", System.currentTimeMillis());
    }
}
