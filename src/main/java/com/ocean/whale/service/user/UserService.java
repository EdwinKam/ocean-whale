package com.ocean.whale.service.user;

import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.AuthCredential;
import com.ocean.whale.model.User;
import com.ocean.whale.repository.FirestoreService;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.util.ObjectConvertor;
import com.ocean.whale.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private FirestoreService firestoreService;
    private AuthService authService;

    @Autowired
    public UserService(FirestoreService firestoreService,
                       AuthService authService) {
        this.firestoreService = firestoreService;
        this.authService = authService;
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

    public void registerUserWithThirdPartyCredentials(String uid) {
        // Fetch username and email
        AuthCredential authCredential = authService.getAuthCredential(uid);
        String username = authCredential.getUsername();
        String email = authCredential.getEmail();

        // Use username if available, fallback to email
        if (StringUtil.isNullOrBlank(username) && StringUtil.isNullOrBlank(email)) {
            throw new WhaleServiceException(WhaleException.NO_USERNAME_TO_CREATE_USER, "failed to use firebase username to create user, need to provide another username manually");
        }

        this.createUser(uid, StringUtil.isNotNullOrBlank(username) ? username : email);
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
