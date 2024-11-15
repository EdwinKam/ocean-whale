package com.ocean.whale.service.auth;

import java.util.Optional;

public interface AuthService {
    Optional<String> getUid(String accessToken) throws Exception;
}
