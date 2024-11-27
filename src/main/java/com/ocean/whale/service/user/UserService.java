package com.ocean.whale.service.user;

import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.User;
import com.ocean.whale.repository.FirestoreService;
import com.ocean.whale.util.ObjectConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private FirestoreService firestoreService;

    @Autowired
    public UserService(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    public boolean isUserRegistered(String uid) {
        // Implement logic to retrieve user by UID from Firestore
        try {
            Map<String, Object> userData = firestoreService.getDocument("user", uid);
            return userData != null;
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR, "error when calling the user table", e);
        }
    }

    public void createUser(String uid, String username) {
        User user = new User();
        user.setUid(uid);
        user.setUsername(username);
        try {
            firestoreService.addDocument("user", user.getUid(), ObjectConvertor.toMap(user));
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR, "error when creating new user in the user table", e);
        }
    }

}
