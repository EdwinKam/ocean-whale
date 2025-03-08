package com.ocean.whale.service.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.firestore.Filter;
import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.Post;
import com.ocean.whale.model.PostComment;
import com.ocean.whale.model.PostLike;
import com.ocean.whale.model.PostRecommendationList;
import com.ocean.whale.repository.FirestoreService;
import com.ocean.whale.service.ImageStorageService;
import com.ocean.whale.util.ObjectConvertor;

@Service
public class PostService {
  private final FirestoreService firestoreService;
  private final ImageStorageService imageStorageService;

  @Autowired
  public PostService(FirestoreService firestoreService, ImageStorageService imageStorageService) {
    this.firestoreService = firestoreService;
    this.imageStorageService = imageStorageService;
  }

  public String createPost(String postSubject, String postContent, String authorId,
      List<MultipartFile> images) {
    Post post = Post.newPost(postContent, postSubject, authorId);
    List<String> imageUrls = savePostImages(post.getId(), images);
    post.setImageUrls(imageUrls);
    firestoreService.addDocument("post", post.getId(), ObjectConvertor.toMap(post));
    return post.getId();
  }

  public List<String> savePostImages(String postId, List<MultipartFile> images) {
    if (images == null) {
      return new ArrayList<>();
    }
    int i = 1;
    List<String> imageUrls = new ArrayList<>();
    for (MultipartFile image : images) {
      String imageId = postId + "_" + i++;
      String imageUrl = imageStorageService.uploadImage(image, imageId);
      imageUrls.add(imageUrl);
    }

    return imageUrls;
  }

  public List<Post> getAllPosts() {
    try {
      return firestoreService.getDocuments("post").stream()
          .map(p -> ObjectConvertor.fromMap(p, Post.class)).toList();
    } catch (WhaleServiceException e) {
      throw e;
    } catch (Exception e) {
      throw new WhaleServiceException(WhaleException.BAD_DATA_ERROR,
          "getAllPost parsing data error");
    }
  }

  public List<Post> getOwnPosts(String requesterUserId) {
    // allow to get more details in the future because this is post owner
    Filter filter = Filter.equalTo("authorId", requesterUserId);
    return getPosts(requesterUserId, filter);
  }

  public List<Post> getBatchPosts(List<String> postIds, String requesterUserId) {
    Filter filter = Filter.inArray("id", postIds);
    return getPosts(requesterUserId, filter);
  }

  public Post getPost(String postId, String requesterUserId) {
    Map<String, Object> databaseValue = firestoreService.getDocument("post", postId);
    Post post = Post.fromMap(databaseValue);
    Optional<PostLike> postLike = getOptionalPostLike(postId, requesterUserId);
    post.setLikedByCurrentUser(postLike.isPresent());

    return post;
  }

  public List<PostComment> getPostComments(String postId) {
    Filter filter = Filter.equalTo("postId", postId);
    List<Map<String, Object>> listOfMap = firestoreService.getDocuments("postComment", filter);

    return listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, PostComment.class)).toList();
  }

  public String addPostComment(PostComment postComment) {
    return firestoreService.addDocument("postComment", postComment.getCommentId(), postComment);
  }

  public void likePost(String postId, String userId) {
    if (!getUserLikedPosts(userId).stream().anyMatch(like -> like.getPostId().equals(postId))) {
      PostLike like = PostLike.create(postId, userId);
      firestoreService.addDocument("postLikes", like.getLikeId(), like);
    }
  }

  public void unlikePost(String postId, String likerId) {
    List<PostLike> userLikes = getUserLikedPosts(likerId);
    PostLike likeToRemove =
        userLikes.stream().filter(like -> like.getPostId().equals(postId)).findFirst().orElse(null);

    if (likeToRemove != null) {
      firestoreService.deleteDocument("postLikes", likeToRemove.getLikeId());
    }
  }

  public long getPostLikeCount(String postId) {
    Filter filter = Filter.equalTo("postId", postId);
    List<Map<String, Object>> listOfMap = firestoreService.getDocuments("postLikes", filter);

    return listOfMap.size();
  }

  public List<PostLike> getUserLikedPosts(String userId) {
    Filter filter = Filter.equalTo("likerId", userId);
    List<Map<String, Object>> listOfMap = firestoreService.getDocuments("postLikes", filter);
    return listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, PostLike.class)).toList();
  }

  public Optional<PostLike> getOptionalPostLike(String postId, String userId) {

    Filter filter = Filter.and(Filter.equalTo("postId", postId), Filter.equalTo("likerId", userId));
    List<Map<String, Object>> listOfMap = firestoreService.getDocuments("postLikes", filter);
    List<PostLike> postLikes =
        listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, PostLike.class)).toList();

    if (postLikes.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(postLikes.get(0));
    }
  }

  private List<Post> getPosts(String requesterUserId, Filter filter) {
    List<Map<String, Object>> listOfMap = firestoreService.getDocuments("post", filter);
    List<Post> posts = listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, Post.class)).toList();

    List<PostLike> userLikes = getUserLikedPosts(requesterUserId);
    Set<String> likedPostIds =
        userLikes.stream().map(PostLike::getPostId).collect(Collectors.toSet());

    posts.forEach(post -> post.setLikedByCurrentUser(likedPostIds.contains(post.getId())));

    return posts;
  }

  public List<Post> getRecommendations(String requesterUserId) {
    Map<String, Object> databaseValue =
        firestoreService.getDocument("recommendations", requesterUserId);
    PostRecommendationList postRecommendationList = PostRecommendationList.fromMap(databaseValue);
    List<Post> posts = getBatchPosts(postRecommendationList.getRecommendations(), requesterUserId);

    return posts;
  }
}
