package com.ocean.whale.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.ocean.whale.model.ApiResponse;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.recommendation.RecommendationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {
  private final RecommendationService recommendationService;

  private final AuthService authService;

  @Autowired
  public RecommendationController(RecommendationService recommendationService, AuthService authService) {
    this.recommendationService = recommendationService;
    this.authService = authService;
  }

  @GetMapping("/get")
  public ResponseEntity<ApiResponse> getRecommendations(@RequestHeader String accessToken)
      throws Exception {
    try {
      // Verify the token and fetch the user UID
      String uid = authService.verifyAndFetchUid(accessToken);
      // Get recommendations based on the UID
      List<String> recommendations = recommendationService.getRecommendations(uid);

      return ResponseEntity.ok(
          new ApiResponse(true, "Recommendations retrieved successfully", recommendations));
    } catch (FirebaseAuthException e) {
      System.err.println("FirebaseAuthException: " + e.getMessage());

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ApiResponse(false, e.getAuthErrorCode().name(), null));
    } catch (IllegalArgumentException e) {
      System.err.println("IllegalArgumentException: " + e.getMessage());

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse(false, e.getMessage(), null));
    } catch (Exception e) {
      System.err.println("Exception: " + e.getMessage());

      return ResponseEntity.internalServerError()
          .body(new ApiResponse(false, e.getMessage(), null));
    }
  }
}
