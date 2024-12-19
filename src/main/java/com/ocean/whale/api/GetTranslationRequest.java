package com.ocean.whale.api;

import lombok.Getter;

@Getter
public class GetTranslationRequest {
    String body;
    String targetLocale;
}
