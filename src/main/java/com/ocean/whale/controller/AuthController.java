package com.ocean.whale.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.ocean.whale.model.ApiResponse;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.user.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;
  private final UserService userService;

  @Autowired
  public AuthController(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  @PostMapping("/verify")
  public ResponseEntity<ApiResponse> verifyAuth(@RequestHeader String accessToken) {
    try {
      String uid = authService.verifyAndFetchUid(accessToken);

      // Check if the user is registered; if not, register them
      if (!userService.isUserRegistered(uid)) {
        registerUser(uid);
      }

      return ResponseEntity.ok(new ApiResponse(true, "User verified successfully", null));
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

  private void registerUser(String uid) throws Exception {
    // Fetch username and email
    Optional<String> usernameOpt = authService.getUsername(uid);
    Optional<String> emailOpt = authService.getEmail(uid);

    // Use username if available, fallback to email
    String entry =
        usernameOpt.orElseGet(
            () ->
                emailOpt.orElseThrow(
                    () -> new IllegalArgumentException("Both username and email are missing")));

    try {
      userService.createUser(uid, entry);
    } catch (Exception e) {
      System.err.println("Error while creating user: " + e.getMessage());
      throw new RuntimeException("Failed to create user with UID: " + uid, e);
    }
  }
}
