package com.ocean.whale.service.post;

import com.google.cloud.firestore.Filter;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.Post;
import com.ocean.whale.repository.FirestoreService;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.util.ObjectConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostService {
  private FirestoreService firestoreService;
  private AuthService authService;

  @Autowired
  public PostService(FirestoreService firestoreService, AuthService authService) {
    this.firestoreService = firestoreService;
    this.authService = authService;
  }

  public String createPost(Post post) {
    firestoreService.addDocument("post", post.getId(), ObjectConvertor.toMap(post));
    return post.getId();
  }

  public List<Map<String, Object>> getAllPosts() throws Exception {
    return firestoreService.getDocuments("post");
  }

  public List<Post> getOwnPosts(String accessToken) {
    String requesterUserId = authService.verifyAndFetchUid(accessToken);

    // allow to get more details in the future because this is post owner
    Filter filter = Filter.equalTo("authorId", requesterUserId);
    List<Map<String, Object>> listOfMap = firestoreService.getDocuments("post", filter);

    return listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, Post.class)).toList();

  }

  public Post getPost(String postId) {
    System.out.println("what is postId");
    System.out.println(postId);
    Map<String, Object> databaseValue = firestoreService.getDocument("post", postId);
    System.out.println(databaseValue);
    return Post.fromMap(databaseValue);
  }
}
