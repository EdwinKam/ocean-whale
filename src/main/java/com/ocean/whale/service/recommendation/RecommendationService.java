package com.ocean.whale.service.recommendation;

import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
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

  public List<String> getRecommendations(String uid) {
    Map<String, Object> userRec;
    try {
      userRec = firestoreService.getDocument("recommendation", uid);
    } catch (Exception e) {
      throw new WhaleServiceException(FIREBASE_ERROR, "error occurred when fetching recommendation table", e);
    }

    // Extract the recommendation list
    List<String> recommendations;
    Object recommendationsObj = userRec.get("recommendations");
    if (recommendationsObj instanceof List<?> list) {
      recommendations =
          list.stream().filter(String.class::isInstance).map(String.class::cast).toList();
    } else {
      throw new WhaleServiceException(BAD_DATA_ERROR, "recommendations object is not a list");
    }

    // Should store an arbitrary number of post for now (e.g. 100)
    return recommendations;
  }
}
