package com.ocean.whale.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerifyAuthResponse {
    private boolean isValidToken;
    private String errorCode;
}
