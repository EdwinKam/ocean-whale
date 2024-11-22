package com.ocean.whale.service.recommendation;

import com.ocean.whale.repository.FirestoreService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecService {
  private final FirestoreService firestoreService;

  @Autowired
  public RecService(FirestoreService firestoreService) {
    this.firestoreService = firestoreService;
  }

  public List<String> getRecommendations(String uid) throws Exception {
    Map<String, Object> userRec = firestoreService.getDocument("recommendation", uid);

    // Extract the recommendation list
    List<String> recommendations;
    Object recommendationsObj = userRec.get("recommendations");
    if (recommendationsObj instanceof List<?> list) {
      recommendations =
          list.stream().filter(String.class::isInstance).map(String.class::cast).toList();
    } else {
      throw new IllegalArgumentException("Recommendations data is not a list.");
    }

    // Should store an arbitrary number of post for now (e.g. 100)
    return recommendations;
  }
}
