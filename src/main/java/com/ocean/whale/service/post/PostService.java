package com.ocean.whale.service.post;

import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.Post;
import com.ocean.whale.repository.FirestoreService;
import com.ocean.whale.util.ObjectConvertor;
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

  public void createPost(Post post) {
    firestoreService.addDocument("post", post.getId(), ObjectConvertor.toMap(post));
  }

  public List<Map<String, Object>> getAllPosts() throws Exception {
    return firestoreService.getDocuments("post");
  }

  public Post getPost(String postId) {
    System.out.println("what is postId");
    System.out.println(postId);
    Map<String, Object> databaseValue = firestoreService.getDocument("post", postId);
    System.out.println(databaseValue);
    return Post.fromMap(databaseValue);
  }
}
