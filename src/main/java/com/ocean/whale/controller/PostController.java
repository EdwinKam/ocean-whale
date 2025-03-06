package com.ocean.whale.controller;

import com.ocean.whale.api.AddCommentRequest;
import com.ocean.whale.api.AddCommentResponse;
import com.ocean.whale.api.CreatePostRequest;
import com.ocean.whale.api.CreatePostResponse;
import com.ocean.whale.api.GetBatchPostResponse;
import com.ocean.whale.api.GetOwnPostResponse;
import com.ocean.whale.api.GetPostCommentsResponse;
import com.ocean.whale.api.GetPostResponse;
import com.ocean.whale.model.Post;
import com.ocean.whale.model.PostComment;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.post.PostService;
import com.ocean.whale.service.view_history.ViewHistoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final AuthService authService;
    private final ViewHistoryService viewHistoryService;

    @Autowired
    public PostController(
            PostService postService, AuthService authService, ViewHistoryService viewHistoryService) {
        this.postService = postService;
        this.authService = authService;
        this.viewHistoryService = viewHistoryService;
    }

    // for testing only
    @PostMapping("/test")
    public String createTestPost(@RequestParam String content, @RequestParam String userId) {
        // curl -X POST "http://localhost:8080/api/test" -d "content=Hello" -d
        // "userId=123"
        try {
            postService.createPost("test subject", content, userId, null);
            return "success creating new post";
        } catch (Exception e) {
            return "error";
        }
    }

    // test api only
    @GetMapping("/test/all")
    public List<Post> getTestPosts() throws Exception {
        return postService.getAllPosts();
    }

    @PostMapping(produces = "application/json")
    public CreatePostResponse createPost(
            @RequestHeader String accessToken, @RequestParam("postContent") String postContent,
            @RequestParam("postSubject") String postSubject,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        String uid = authService.verifyAndFetchUid(accessToken);
        String postId = postService.createPost(postSubject, postContent, uid, image == null ? List.of() : List.of(image));

        return new CreatePostResponse(postId);
    }

    @GetMapping("/{postId}")
    public GetPostResponse getPost(@RequestHeader String accessToken, @PathVariable String postId) {
        String uid = authService.verifyAndFetchUid(accessToken);

        Post post = postService.getPost(postId, uid);
        viewHistoryService.userReadPost(postId, uid);

        return new GetPostResponse(post);
    }

    @GetMapping("/batch")
    public GetBatchPostResponse getBatchPosts(
            @RequestHeader String accessToken, @RequestParam List<String> postIds) {
        String requesterUserId = authService.verifyAndFetchUid(accessToken);

        List<Post> posts = postService.getBatchPosts(postIds, requesterUserId);
        GetBatchPostResponse response = new GetBatchPostResponse();
        response.setPosts(posts);

        return response;
    }

    @GetMapping("/me")
    public GetOwnPostResponse getOwnPosts(@RequestHeader String accessToken) {
        String requesterUserId = authService.verifyAndFetchUid(accessToken);

        List<Post> posts = postService.getOwnPosts(requesterUserId);

        GetOwnPostResponse response = new GetOwnPostResponse();
        response.setPosts(posts);

        return response;
    }

    @GetMapping("/{postId}/comments")
    public GetPostCommentsResponse getComments(
            @RequestHeader String accessToken, @PathVariable String postId) {
        GetPostCommentsResponse response = new GetPostCommentsResponse();
        response.setPostComments(postService.getPostComments(postId));

        return response;
    }

    @PostMapping(value = "/{postId}/comments", produces = "application/json")
    public AddCommentResponse addComment(
            @RequestHeader String accessToken,
            @PathVariable String postId,
            @RequestBody AddCommentRequest request) {
        String uid = authService.verifyAndFetchUid(accessToken);
        PostComment postComment = PostComment.create(postId, request.getContent(), uid, request.getParentCommentId());
        postService.addPostComment(postComment);

        return new AddCommentResponse(postComment.getCommentId());
    }

    @PostMapping("/{postId}/likes")
    public void likePost(@RequestHeader String accessToken, @PathVariable String postId) {
        String uid = authService.verifyAndFetchUid(accessToken);
        postService.likePost(postId, uid);
    }

    @DeleteMapping("/{postId}/likes")
    public void unlikePost(@RequestHeader String accessToken, @PathVariable String postId) {
        String uid = authService.verifyAndFetchUid(accessToken);
        postService.unlikePost(postId, uid);
    }

    @GetMapping("/{postId}/likes/count")
    public long getPostLikeCount(@RequestHeader String accessToken, @PathVariable String postId) {
        return postService.getPostLikeCount(postId);
    }
}
