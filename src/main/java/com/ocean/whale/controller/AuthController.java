package com.ocean.whale.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.ocean.whale.model.VerifyAuthResponse;
import com.ocean.whale.service.auth.GoogleAuthService;
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

    @Autowired
    public AuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    // TODO change to postmapping, move param to header
    @GetMapping("/verify")
    public VerifyAuthResponse verifyAuth(@RequestParam String accessToken) {
        // http://localhost:8080/auth/verify?accessToken=eyJxxxx
        VerifyAuthResponse verifyAuthResponse = new VerifyAuthResponse();
        try {
            Optional<String> uid = googleAuthService.getUid(accessToken);
            verifyAuthResponse.setValidToken(uid.isPresent() && !uid.get().isBlank());
        } catch (FirebaseAuthException e) {
            verifyAuthResponse.setErrorCode(e.getAuthErrorCode().name());
        } catch (Exception e) {
            //
        }
        return verifyAuthResponse;
    }
}
