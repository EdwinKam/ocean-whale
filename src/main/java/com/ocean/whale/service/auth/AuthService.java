package com.ocean.whale.service.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  // Verifies the token and fetches the UID
  public String verifyAndFetchUid(String accessToken) throws FirebaseAuthException {
    FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(accessToken);
    String uid = decodedToken.getUid();

    if (uid.isBlank()) {
      throw new IllegalArgumentException("Invalid UID: UID is null or blank");
    }
    return uid;
  }

  // Fetches the username for a given UID
  public Optional<String> getUsername(String uid) throws FirebaseAuthException {
    UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
    return Optional.ofNullable(userRecord.getDisplayName());
  }

  // Fetches the email for a given UID
  public Optional<String> getEmail(String uid) throws FirebaseAuthException {
    UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
    return Optional.ofNullable(userRecord.getEmail());
  }
}
