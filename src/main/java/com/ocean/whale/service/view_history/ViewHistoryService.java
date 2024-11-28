package com.ocean.whale.service.view_history;

import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.repository.FirestoreService;
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
        String key = "READ".concat(userId).concat(postId);
        Map<String, Object> value = Map.of("timestamp", System.currentTimeMillis());

        try {
            firestoreService
                    .addDocument("viewHistory", key, value);
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR, "cannot create read history", e);
        }
    }
}
