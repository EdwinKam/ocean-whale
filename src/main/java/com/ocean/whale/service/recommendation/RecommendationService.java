package com.ocean.whale.service.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ocean.whale.exception.WhaleException.FIREBASE_ERROR;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.PostRecommendationList;
import com.ocean.whale.repository.FirestoreService;

@Service
public class RecommendationService {
  private final FirestoreService firestoreService;

  @Autowired
  public RecommendationService(FirestoreService firestoreService) {
    this.firestoreService = firestoreService;
  }

  public void addRecommendations(String uid, PostRecommendationList postRecommendationList) {
    try {
      firestoreService.addDocument("recommendation", uid, postRecommendationList);
    } catch (Exception e) {
      throw new WhaleServiceException(FIREBASE_ERROR,
          "error occurred when fetching recommendation table", e);
    }
  }
}
