package com.ocean.whale.model;

import com.ocean.whale.util.ObjectConvertor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
  private String id;
  private String content;
  private String authorId;

  public static Post newPost(String content, String authorId) {
    return new Post(UUID.randomUUID().toString().substring(0, 8), content, authorId);
  }

  public static Post fromMap(Map<String, Object> map) {
    return ObjectConvertor.fromMap(map, Post.class);
  }
}
