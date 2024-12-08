package com.ocean.whale.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VerifyAuthResponse {
  private boolean isValidToken;
}