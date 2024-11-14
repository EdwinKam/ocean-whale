package com.ocean.whale.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class Post {
  private Integer id;
  private String content;
  private Integer authorId;

  public Map<String, Object> toMap() {
    return Map.of("userId", authorId, "postId", id, "content", content);
  }
}
