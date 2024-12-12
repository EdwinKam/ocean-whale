package com.ocean.whale.controller;

import com.ocean.whale.api.CreatePostRequest;
import com.ocean.whale.api.GetBatchPostResponse;
import com.ocean.whale.api.CreatePostResponse;
import com.ocean.whale.api.GetPostResponse;
import com.ocean.whale.model.Post;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.post.PostService;
import com.ocean.whale.service.view_history.ViewHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
            postService.createPost(Post.newPost(content, userId));
            return "success creating new post";
        } catch (Exception e) {
            return "error";
        }
    }

    // test api only
    @GetMapping("/getPostsForTesting")
    public List<Map<String, Object>> getPosts() throws Exception {
        return postService.getAllPosts();
    }

    @PostMapping(value = "/create", produces = "application/json")
    public CreatePostResponse createPost(@RequestHeader String accessToken, @RequestBody CreatePostRequest request) {
        String uid = authService.verifyAndFetchUid(accessToken);
        String postId = postService.createPost(Post.newPost(request.getPostContent(), uid));

        return new CreatePostResponse(postId);
    }

    @GetMapping("/get")
    public GetPostResponse getPost(@RequestHeader String accessToken, @RequestParam String postId) {
        String uid = authService.verifyAndFetchUid(accessToken);

        Post post = postService.getPost(postId);
        viewHistoryService.userReadPost(postId, uid);

        return new GetPostResponse(post);
    }

    @GetMapping("/getBatchPost")
    public GetBatchPostResponse getBatchPost(@RequestHeader String accessToken, @RequestParam List<String> postIds) {
        String requesterUserId = authService.verifyAndFetchUid(accessToken);

        List<Post> posts = postIds.stream().map(postService::getPost).toList();

        GetBatchPostResponse response = new GetBatchPostResponse();
        response.setPosts(posts);

        return response;
    }
}
