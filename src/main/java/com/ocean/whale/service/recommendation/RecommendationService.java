package com.ocean.whale.service.recommendation;

import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.PostRecommendationList;
import com.ocean.whale.repository.FirestoreService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ocean.whale.exception.WhaleException.BAD_DATA_ERROR;
import static com.ocean.whale.exception.WhaleException.FIREBASE_ERROR;

@Service
public class RecommendationService {
  private final FirestoreService firestoreService;

  @Autowired
  public RecommendationService(FirestoreService firestoreService) {
    this.firestoreService = firestoreService;
  }

  public PostRecommendationList getRecommendations(String uid) {
    Map<String, Object> userRec;
    try {
      userRec = firestoreService.getDocument("recommendation", uid);
    } catch (Exception e) {
      throw new WhaleServiceException(FIREBASE_ERROR, "error occurred when fetching recommendation table", e);
    }

    return PostRecommendationList.fromMap(userRec);
  }
}
