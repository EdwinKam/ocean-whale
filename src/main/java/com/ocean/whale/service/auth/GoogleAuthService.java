package com.ocean.whale.service.auth;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.ocean.whale.model.VerifyAuthResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoogleAuthService implements AuthService {
    @Override
    public Optional<String> getUid(String accessToken) throws Exception {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(accessToken);
        return Optional.ofNullable(decodedToken.getUid());
    }
}
