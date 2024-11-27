package com.ocean.whale.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VerifyAuthResponse {
  private boolean isValidToken;
}