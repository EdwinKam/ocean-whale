package com.ocean.whale.service;

import com.ocean.whale.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostService {
  private FirestoreService firestoreService;

  @Autowired
  public PostService(FirestoreService firestoreService) {
    this.firestoreService = firestoreService;
  }

  public void createPost(Post post) throws Exception {
    firestoreService.addDocument("post", post.getId().toString(), post.toMap());
  }

  public List<Map<String, Object>> getAllPosts() throws Exception {
    return firestoreService.getDocuments("post");
  }
}
