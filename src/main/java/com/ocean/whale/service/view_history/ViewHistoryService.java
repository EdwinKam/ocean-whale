package com.ocean.whale.service.view_history;

import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.PostViewHistory;
import com.ocean.whale.repository.FirestoreService;
import com.ocean.whale.util.ObjectConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ViewHistoryService {
    private final FirestoreService firestoreService;

    @Autowired
    public ViewHistoryService(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    public void userReadPost(String postId, String userId) {
        PostViewHistory postViewHistory = PostViewHistory.readHistory(postId, userId);

        try {
            firestoreService
                    .addDocument("viewHistory", postViewHistory.getPostViewHistoryId(), ObjectConvertor.toMap(postViewHistory));
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR, "cannot create read history", e);
        }
    }
}
