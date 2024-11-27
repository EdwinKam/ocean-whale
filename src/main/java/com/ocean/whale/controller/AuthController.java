package com.ocean.whale.controller;

import com.ocean.whale.model.VerifyAuthResponse;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.user.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
    public VerifyAuthResponse verifyAuth(@RequestHeader String accessToken) {
        String uid = authService.verifyAndFetchUid(accessToken);

        // Check if the user is registered; if not, register them
        if (!userService.isUserRegistered(uid)) {
            registerUser(uid);
        }

        return new VerifyAuthResponse(true);
    }

    private void registerUser(String uid) {
        // Fetch username and email
        Optional<String> usernameOpt = authService.getUsername(uid);
        Optional<String> emailOpt = authService.getEmail(uid);

        // Use username if available, fallback to email
        String entry = usernameOpt.orElseGet(() -> emailOpt.orElseThrow(() -> new IllegalArgumentException("Both username and email are missing")));

        userService.createUser(uid, entry);
    }
}
