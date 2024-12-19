package com.ocean.whale.controller;

import com.ocean.whale.api.CreatePostRequest;
import com.ocean.whale.api.GetTranslationRequest;
import com.ocean.whale.service.translation.TranslationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/translation")
public class TranslationController {
    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping(value = "/translation", produces = "application/json")
    public String translation(@RequestBody GetTranslationRequest request) {
        translationService.translateText(request.getBody(), request.getTargetLocale());
        return "success";
    }
}
