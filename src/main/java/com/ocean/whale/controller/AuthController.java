package com.ocean.whale.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.ocean.whale.model.VerifyAuthResponse;
import com.ocean.whale.service.auth.GoogleAuthService;
import com.ocean.whale.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final GoogleAuthService googleAuthService;
    private final UserService userService;

    @Autowired
    public AuthController(GoogleAuthService googleAuthService, UserService userService) {
        this.googleAuthService = googleAuthService;
        this.userService = userService;
    }

    // TODO change to postmapping, move param to header
    @GetMapping("/verify")
    public VerifyAuthResponse verifyAuth(@RequestParam String accessToken) {
        // http://localhost:8080/auth/verify?accessToken=eyJxxxx
        VerifyAuthResponse verifyAuthResponse = new VerifyAuthResponse();
        try {
            Optional<String> uidOpt = googleAuthService.getUid(accessToken);
            if (uidOpt.isPresent()) {
                String uid = uidOpt.get();
                if (uid.isBlank()) {
                    verifyAuthResponse.setValidToken(false);
                    verifyAuthResponse.setErrorCode("Invalid UID: UID is null or blank");

                    return verifyAuthResponse;
                }

                verifyAuthResponse.setValidToken(true);

                // Check if the user already exists
                if (!userService.isUserRegistered(uid)) {
                    String username = googleAuthService.getUsername(uid);
                    userService.createUser(uid, username);
                }
            } else {
                verifyAuthResponse.setValidToken(false);
                verifyAuthResponse.setErrorCode("Invalid Token: UID not present");
            }
        } catch (FirebaseAuthException e) {
            verifyAuthResponse.setErrorCode(e.getAuthErrorCode().name());
        } catch (Exception e) {
            //
        }
        return verifyAuthResponse;
    }
}
