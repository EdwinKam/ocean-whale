package com.ocean.whale.model;

import java.util.List;
import java.util.Map;

import com.ocean.whale.util.IdGenerator;
import com.ocean.whale.util.ObjectConvertor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private String id;
    private String content;
    private String subject;
    private String authorId;
    private long creationTs;
    private List<String> imageUrls;
    private boolean isLikedByCurrentUser;

    public static Post newPost(String content, String subject, String authorId) {
        return new Post(IdGenerator.length(8), content, subject, authorId, System.currentTimeMillis(), null, false);
    }

    public static Post fromMap(Map<String, Object> map) {
        return ObjectConvertor.fromMap(map, Post.class);
    }

    public Boolean equals(Post other) {
        return this.getId().equals(other.getId()) &&
                this.getAuthorId().equals(other.getAuthorId()) &&
                this.getContent().equals(other.getContent()) &&
                this.getSubject().equals(other.getSubject());
    }

    ;
}
