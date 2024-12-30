package com.ocean.whale.controller;

import com.ocean.whale.api.CreatePostRequest;
import com.ocean.whale.api.CreatePostResponse;
import com.ocean.whale.api.GetOwnPostResponse;
import com.ocean.whale.api.GetPostResponse;
import com.ocean.whale.model.Post;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.post.PostService;
import com.ocean.whale.service.view_history.ViewHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Mock
    private PostService postService;
    @InjectMocks
    private PostController postController;
    @Mock
    private AuthService authService;
    @Mock
    private Post post;
    @Mock
    private Post secondPost;
    @Mock
    private ViewHistoryService viewHistoryService;
    @Captor
    private ArgumentCaptor<Post> postCaptor;

    private final String accessToken = "test-accessToken";
    private final String UID = "testuid";
    private final String TEST_POST_ID = "testpostid";
    private final String SECOND_TEST_POST_ID = "secondpostid";

    @BeforeEach
    void setUp() {
        Mockito.when(authService.verifyAndFetchUid(accessToken)).thenReturn(UID);
    }

//    @Test
//    void createPost() {
//        CreatePostRequest createPostRequest = new CreatePostRequest();
//        createPostRequest.setPostContent("testcontent");
//        Mockito.when(postService.createPost(Mockito.any(Post.class))).thenReturn(TEST_POST_ID);
//
//        CreatePostResponse response = postController.createPost(accessToken, createPostRequest);
//
//        Mockito.verify(postService).createPost(postCaptor.capture());
//        Post post = postCaptor.getValue();
//        assertNotEquals("", post.getId()); // this should be a random value
//        assertEquals(TEST_POST_ID, response.getPostId()); // this post id is returned from postService
//        assertEquals("testcontent", post.getContent());
//        assertEquals(UID, post.getAuthorId());
//    }

    @Test
    void readPost() {
        Mockito.when(postService.getPost(TEST_POST_ID)).thenReturn(post);

        GetPostResponse response = postController.readPost(accessToken, TEST_POST_ID);

        assertEquals(post, response.getPost());
    }

//    @Test
//    void getBatchPost() {
//        Mockito.when(postService.readPost(TEST_POST_ID)).thenReturn(post);
//        Mockito.when(postService.readPost(SECOND_TEST_POST_ID)).thenReturn(secondPost);
//
//        GetBatchPostResponse response = postController.getBatchPost(accessToken, List.of(TEST_POST_ID, SECOND_TEST_POST_ID));
//
//        assertTrue(response.getPosts().contains(post));
//        assertTrue(response.getPosts().contains(secondPost));
//    }

    @Test
    void getOwnPosts() {
        Mockito.reset(authService); // this function no need authService, clear the stubbing
        Mockito.when(postService.getOwnPosts(accessToken)).thenReturn(List.of(post, secondPost));

        GetOwnPostResponse response = postController.getOwnPosts(accessToken);

        assertTrue(response.getPosts().contains(post));
        assertTrue(response.getPosts().contains(secondPost));
    }
}