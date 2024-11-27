package com.ocean.whale.service.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import java.util.Optional;

import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.AuthCredential;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  // Verifies the token and fetches the UID
  public String verifyAndFetchUid(String accessToken) throws WhaleServiceException {
    try {
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(accessToken);
      String uid = decodedToken.getUid();

      if (uid.isBlank()) {
        throw new WhaleServiceException(WhaleException.UNAUTHENTICATED, "empty uid retrieved from firebase", null);
      }
      return uid;
    } catch (Exception e) {
      throw new WhaleServiceException(WhaleException.UNAUTHENTICATED, "firebase exception", e);
    }
  }

  // Fetches the username for a given UID
  public AuthCredential getAuthCredential(String uid) {
    try {
      UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
      return new AuthCredential(uid, userRecord.getDisplayName(), userRecord.getEmail());
    } catch (Exception e) {
      throw new WhaleServiceException(WhaleException.FIREBASE_ERROR, "get auth credential error", e);
    }
  }
}
