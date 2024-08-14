package com.SoT.JIN.story;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {
    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> predictThemes(String title, String location, String description, String tags) {
        String url = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String[] themes = {"자연 속 여행", "역사와 문화", "식도락 여행", "축제", "예술 및 체험", "산악 여행", "도심 속 여행", "바다와 해변", "테마파크"};
        String themesList = String.join(", ", themes);

        String prompt = String.format(
                "Title: %s\nLocation: %s\nDescription: %s\nTags: %s\n\nPlease list all applicable themes from the following list: %s.",
                title, location, description, tags, themesList);

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo");
        request.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        request.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        int retryCount = 0;
        int maxRetries = 5;
        long waitTime = 2000; // 2초

        while (retryCount < maxRetries) {
            try {
                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
                List<String> extractedThemes = extractThemes(response, themes);
                if (!extractedThemes.isEmpty()) {
                    return extractedThemes;
                } else {
                    return List.of("Invalid or no themes returned");
                }
            } catch (HttpClientErrorException.TooManyRequests e) {
                retryCount++;
                if (retryCount == maxRetries) {
                    throw e;
                }
                try {
                    Thread.sleep(waitTime);
                    waitTime *= 2; // 지수적으로 대기 시간 증가
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted", ie);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return List.of("API 호출 실패: " + e.getMessage());
            }
        }
        return List.of("API 호출 실패: 최대 재시도 횟수 초과");
    }

    private List<String> extractThemes(ResponseEntity<Map> response, String[] validThemes) {
        List<String> validThemesList = new ArrayList<>();
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        String content = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
        String[] predictedThemes = content.split(",");

        for (String theme : predictedThemes) {
            theme = theme.trim(); // 공백 제거
            if (isValidTheme(theme, validThemes)) {
                validThemesList.add(theme);
            }
        }
        return validThemesList;
    }

    private boolean isValidTheme(String theme, String[] validThemes) {
        for (String validTheme : validThemes) {
            if (validTheme.equals(theme)) {
                return true;
            }
        }
        return false;
    }
}
