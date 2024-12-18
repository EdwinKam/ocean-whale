package com.ocean.whale.model;

import com.ocean.whale.util.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PostComment {
    String commentId;
    String postId;
    String content;
    String commenterUid;
    String parentCommentId;
    long createTime;

    public static PostComment create(String postId, String content, String commenterUid, String parentCommentId) {
        String newCommentId = IdGenerator.length(16);
        long timestamp = System.currentTimeMillis();
        return new PostComment(newCommentId, postId, content, commenterUid, parentCommentId, timestamp);
    }
}
