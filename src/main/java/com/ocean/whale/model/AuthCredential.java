package com.ocean.whale.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthCredential {
    String uid;
    String username;
    String email;
}
