package com.ocean.whale.service.post;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Filter;
import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.Post;
import com.ocean.whale.model.PostComment;
import com.ocean.whale.model.PostLike;
import com.ocean.whale.repository.FirestoreService;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.util.ObjectConvertor;

@Service
public class PostService {
    private final FirestoreService firestoreService;
    private final AuthService authService;

    @Autowired
    public PostService(FirestoreService firestoreService, AuthService authService) {
        this.firestoreService = firestoreService;
        this.authService = authService;
    }

    public String createPost(Post post) {
        firestoreService.addDocument("post", post.getId(), ObjectConvertor.toMap(post));
        return post.getId();
    }

    public List<Post> getAllPosts() {
        try {
            return firestoreService.getDocuments("post").stream()
                    .map(p -> ObjectConvertor.fromMap(p, Post.class))
                    .toList();
        } catch (WhaleServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.BAD_DATA_ERROR, "getAllPost parsing data error");
        }
    }

    public List<Post> getOwnPosts(String requesterUserId) {
        // allow to get more details in the future because this is post owner
        Filter filter = Filter.equalTo("authorId", requesterUserId);
        List<Map<String, Object>> listOfMap = firestoreService.getDocuments("post", filter);
        List<Post> posts = listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, Post.class)).toList();

        List<PostLike> userLikes = userLikedPosts(requesterUserId);
        Set<String> likedPostIds = userLikes.stream()
                .map(PostLike::getPostId)
                .collect(Collectors.toSet());

        posts.forEach(post -> post.setLikedByCurrentUser(likedPostIds.contains(post.getId())));

        return posts;
    }

    public List<Post> getBatchPosts(List<String> postIds, String requesterUserId) {
        Filter filter = Filter.inArray("id", postIds);
        List<Map<String, Object>> listOfMap = firestoreService.getDocuments("post", filter);
        List<Post> posts = listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, Post.class)).toList();

        List<PostLike> userLikes = userLikedPosts(requesterUserId);
        Set<String> likedPostIds = userLikes.stream()
                .map(PostLike::getPostId)
                .collect(Collectors.toSet());
        posts.forEach(post -> post.setLikedByCurrentUser(likedPostIds.contains(post.getId())));

        return posts;
    }

    public Post getPost(String postId, String requesterUserId) {
        Map<String, Object> databaseValue = firestoreService.getDocument("post", postId);
        Post post = Post.fromMap(databaseValue);
        post.setLikedByCurrentUser(!userLikedPosts(requesterUserId).stream()
                .anyMatch(like -> like.getPostId().equals(postId)));

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
        if (!userLikedPosts(userId).stream().anyMatch(like -> like.getPostId().equals(postId))) {
            PostLike like = PostLike.create(postId, userId);
            try {
                firestoreService.addDocument("postLikes", like.getLikeId(), like);
                incrementPostLikeCount(postId);
            } catch (Exception e) {
                throw new WhaleServiceException(WhaleException.FIREBASE_ERROR,
                        "Error while liking post", e);
            }
        }
    }

    public void unlikePost(String postId, String likerId) {
        try {
            List<PostLike> userLikes = userLikedPosts(likerId);
            PostLike likeToRemove = userLikes.stream()
                    .filter(like -> like.getPostId().equals(postId))
                    .findFirst()
                    .orElse(null);

            if (likeToRemove != null) {
                firestoreService.deleteDocument("postLikes", likeToRemove.getLikeId());
                decrementPostLikeCount(postId);
            }

        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR,
                    "Error while unliking post", e);
        }
    }

    public List<PostLike> userLikedPosts(String userId) {
        try {
            Filter filter = Filter.equalTo("likerId", userId);
            List<Map<String, Object>> listOfMap = firestoreService.getDocuments("postLikes", filter);
            return listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, PostLike.class)).toList();
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR,
                    "Error while checking liked posts for user", e);
        }
    }

    private void incrementPostLikeCount(String postId) {
        try {
            firestoreService.updateDocument(
                    "post",
                    postId,
                    Map.of("likes", FieldValue.increment(1)));
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR,
                    "Error while incrementing post like count", e);
        }
    }

    private void decrementPostLikeCount(String postId) {
        try {
            firestoreService.updateDocument(
                    "post",
                    postId,
                    Map.of("likes", FieldValue.increment(-1)));
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR,
                    "Error while decrementing post like count", e);
        }
    }
}
