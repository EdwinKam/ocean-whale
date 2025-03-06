package com.ocean.whale.model;

import com.ocean.whale.util.IdGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PostLike {

    private String likeId;
    private String likerId;
    private String postId;
    private long createTime;

    public static PostLike create(String postId, String likerId) {
        String newLikeID = IdGenerator.length(16);
        long timestamp = System.currentTimeMillis();

        return new PostLike(newLikeID, likerId, postId, timestamp);
    }
}
