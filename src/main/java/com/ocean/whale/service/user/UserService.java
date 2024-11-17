package com.ocean.whale.service.user;

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

    public boolean isUserRegistered(String uid) throws Exception {
        // Implement logic to retrieve user by UID from Firestore
        Map<String, Object> userData = firestoreService.getDocument("user", uid);

        return userData != null;
    }

    public void createUser(String uid, String username) throws Exception {
        User user = new User();
        user.setUid(uid);
        user.setUsername(username);
        firestoreService.addDocument("user", user.getUid(), ObjectConvertor.toMap(user));
    }

}
