package com.ocean.whale.controller;

import com.ocean.whale.model.VerifyAuthResponse;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.user.UserService;

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
            userService.registerUserWithThirdPartyCredentials(uid);
        }

        return new VerifyAuthResponse(true);
    }


}
