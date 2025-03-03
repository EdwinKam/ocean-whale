package com.ocean.whale.service.post;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.cloud.firestore.Filter;
import com.ocean.whale.model.Post;
import com.ocean.whale.repository.FirestoreService;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.util.ObjectConvertor;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private AuthService authService;
    @Mock
    private FirestoreService firestoreService;
    @Captor
    private ArgumentCaptor<Filter> filterArgumentCaptor;
    @InjectMocks
    private PostService postService;

    private String accessToken = "access-token";
    private String uid = "sampleuid";

    @Test
    void getOwnPosts() {
        Post post1 = Post.newPost("haha", "hehe", uid);
        Post post2 = Post.newPost("yoyo", "hoho", uid);
        List<Map<String, Object>> databaseValues = List.of(ObjectConvertor.toMap(post1), ObjectConvertor.toMap(post2));
        Mockito.when(firestoreService.getDocuments(Mockito.eq("post"), Mockito.any(Filter.class)))
                .thenReturn(databaseValues);

        List<Post> ownPosts = postService.getOwnPosts(accessToken);

        Mockito.verify(firestoreService).getDocuments(Mockito.eq("post"), filterArgumentCaptor.capture());
        assertEquals(2, ownPosts.size());
        assertTrue(post1.equals(ownPosts.get(0)));
        assertTrue(post2.equals(ownPosts.get(1)));
    }
}
