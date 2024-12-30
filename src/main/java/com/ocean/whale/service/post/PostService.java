package com.ocean.whale.service.post;

import com.google.cloud.firestore.Filter;
import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.Post;
import com.ocean.whale.model.PostComment;
import com.ocean.whale.repository.FirestoreService;
import com.ocean.whale.service.ImageStorageService;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.util.ObjectConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PostService {
    private FirestoreService firestoreService;
    private AuthService authService;
    private ImageStorageService imageStorageService;

    @Autowired
    public PostService(FirestoreService firestoreService, AuthService authService, ImageStorageService imageStorageService) {
        this.firestoreService = firestoreService;
        this.authService = authService;
        this.imageStorageService = imageStorageService;
    }

    public String createPost(String postSubject, String postContent, String authorId, List<MultipartFile> images) {
        Post post = Post.newPost(postContent, postSubject, authorId);
        List<String> imageIds = savePostImages(post.getId(), images);
        post.setImages(imageIds);
//        firestoreService.addDocument("post", post.getId(), ObjectConvertor.toMap(post));
        return post.getId();
    }

    public List<String> savePostImages(String postId, List<MultipartFile> images) {
        int i = 1;
        List<String> imageIds = new ArrayList<>();
        for (MultipartFile image : images) {
            String imageId = postId + "_" + i++;
            imageStorageService.uploadImage(image, imageId);
            imageIds.add(imageId);
        }

        return imageIds;
    }

    public List<Post> getAllPosts() {
        try {
            return firestoreService.getDocuments("post").stream().map(p -> ObjectConvertor.fromMap(p, Post.class)).toList();
        } catch (WhaleServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.BAD_DATA_ERROR, "getAllPost parsing data error");
        }
    }

    public List<Post> getOwnPosts(String accessToken) {
        String requesterUserId = authService.verifyAndFetchUid(accessToken);

        // allow to get more details in the future because this is post owner
        Filter filter = Filter.equalTo("authorId", requesterUserId);
        List<Map<String, Object>> listOfMap = firestoreService.getDocuments("post", filter);

        return listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, Post.class)).toList();
    }

    public List<Post> getBatchPosts(List<String> postIds) {
        Filter filter = Filter.inArray("id", postIds);
        List<Map<String, Object>> listOfMap = firestoreService.getDocuments("post", filter);

        return listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, Post.class)).toList();
    }

    public Post getPost(String postId) {
        Map<String, Object> databaseValue = firestoreService.getDocument("post", postId);
        return Post.fromMap(databaseValue);
    }

    public List<PostComment> getPostComments(String postId) {
        Filter filter = Filter.equalTo("postId", postId);
        List<Map<String, Object>> listOfMap = firestoreService.getDocuments("postComment", filter);

        return listOfMap.stream().map(m -> ObjectConvertor.fromMap(m, PostComment.class)).toList();
    }

    public String addPostComment(PostComment postComment) {
        return firestoreService.addDocument("postComment", postComment.getCommentId(), postComment);
    }
}
