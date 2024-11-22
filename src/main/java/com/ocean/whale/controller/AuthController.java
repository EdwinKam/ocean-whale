package com.ocean.whale.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuthException;
import com.ocean.whale.model.VerifyAuthResponse;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.user.UserService;

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

    // TODO change to postmapping, move param to header
    @GetMapping("/verify")
    public VerifyAuthResponse verifyAuth(@RequestHeader String accessToken) {
        // http://localhost:8080/api/auth/verify?accessToken=eyJxxxx
        VerifyAuthResponse verifyAuthResponse = new VerifyAuthResponse();
        try {
            Optional<String> uidOpt = authService.getUid(accessToken);
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
                    Optional<String> username = authService.getUsername(uid);
                    if (username.isEmpty()) {
                        username = authService.getEmail(uid);
                    }
                    String entry = username.orElseThrow(() -> new IllegalArgumentException("Value is missing"));
                    userService.createUser(uid, entry);
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
