package com.ocean.whale.controller;

import com.ocean.whale.api.AddCommentRequest;
import com.ocean.whale.api.AddCommentResponse;
import com.ocean.whale.api.CreatePostRequest;
import com.ocean.whale.api.GetBatchPostResponse;
import com.ocean.whale.api.CreatePostResponse;
import com.ocean.whale.api.GetPostCommentsResponse;
import com.ocean.whale.api.GetOwnPostResponse;
import com.ocean.whale.api.GetPostResponse;
import com.ocean.whale.model.Post;
import com.ocean.whale.model.PostComment;
import com.ocean.whale.service.ImageStorageService;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.post.PostService;
import com.ocean.whale.service.user.UserService;
import com.ocean.whale.service.view_history.ViewHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final AuthService authService;
    private final ViewHistoryService viewHistoryService;

    @Autowired
    public PostController(PostService postService, AuthService authService, ViewHistoryService viewHistoryService) {
        this.postService = postService;
        this.authService = authService;
        this.viewHistoryService = viewHistoryService;
    }

    // for testing only
    @PostMapping("/createPostForTesting")
    public String createPostForTesting(@RequestParam String content, @RequestParam String userId) {
        // curl -X POST "http://localhost:8080/api/createPost" -d "content=Hello" -d "userId=123"
        try {
            postService.createPost("test subject", content, userId, null);
            return "success creating new post";
        } catch (Exception e) {
            return "error";
        }
    }

    // test api only
    @GetMapping("/getPostsForTesting")
    public List<Post> getPosts() throws Exception {
        return postService.getAllPosts();
    }

    @PostMapping(value = "/create", produces = "application/json")
    public CreatePostResponse createPost(
            @RequestHeader String accessToken,
            @RequestParam("postContent") String postContent,
            @RequestParam("postSubject") String postSubject,
            @RequestParam("image") MultipartFile image) {

        String uid = authService.verifyAndFetchUid(accessToken);
        String postId = postService.createPost(postSubject, postContent, uid, List.of(image));
        return new CreatePostResponse(postId);
    }

    @GetMapping("/readPost")
    public GetPostResponse readPost(@RequestHeader String accessToken, @RequestParam String postId) {
        String uid = authService.verifyAndFetchUid(accessToken);

        Post post = postService.getPost(postId);
        viewHistoryService.userReadPost(postId, uid);

        return new GetPostResponse(post);
    }

    @GetMapping("/getBatchPost")
    public GetBatchPostResponse getBatchPost(@RequestHeader String accessToken, @RequestParam List<String> postIds) {
        String requesterUserId = authService.verifyAndFetchUid(accessToken);

        List<Post> posts = postService.getBatchPosts(postIds);

        GetBatchPostResponse response = new GetBatchPostResponse();
        response.setPosts(posts);

        return response;
    }

    @GetMapping("/getOwnPosts")
    public GetOwnPostResponse getOwnPosts(@RequestHeader String accessToken) {
        List<Post> posts = postService.getOwnPosts(accessToken);

        GetOwnPostResponse response = new GetOwnPostResponse();
        response.setPosts(posts);

        return response;
    }

    @GetMapping("getComments")
    public GetPostCommentsResponse getComments(@RequestHeader String accessToken, @RequestParam String postId) {
        GetPostCommentsResponse response = new GetPostCommentsResponse();
        response.setPostComments(postService.getPostComments(postId));

        return response;
    }

    @PostMapping(value = "/addComment", produces = "application/json")
    public AddCommentResponse addComment(@RequestHeader String accessToken, @RequestBody AddCommentRequest request) {
        String uid = authService.verifyAndFetchUid(accessToken);
        PostComment postComment = PostComment.create(request.getPostId(), request.getContent(), uid, request.getParentCommentId());
        postService.addPostComment(postComment);

        return new AddCommentResponse(postComment.getCommentId());
    }
}
