package com.ocean.whale.service.auth;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

@Service
public class AuthService {
    public Optional<String> getUid(String accessToken) throws Exception {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(accessToken);
        return Optional.ofNullable(decodedToken.getUid());
    }

    public Optional<String> getUsername(String uid) throws Exception {
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);

        return Optional.ofNullable(userRecord.getDisplayName());
    }

    public Optional<String> getEmail(String uid) throws Exception {
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);

        return Optional.ofNullable(userRecord.getEmail());
    }
}
