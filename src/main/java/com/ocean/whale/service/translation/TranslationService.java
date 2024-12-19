package com.ocean.whale.service.translation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;

@Service
public class TranslationService {
    @Value("${google.translate.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void translateText(String originalText, String targetLocale) {
        String url = "https://translation.googleapis.com/language/translate/v2?key=" + apiKey;

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("q", originalText);
        body.put("target", targetLocale);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.printf(response.getBody());
    }
}
