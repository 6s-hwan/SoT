package com.SoT.JIN.story;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

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

    public String predictTheme(String title, String location, String description, String tags) {
        String url = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String[] themes = {"자연 속 여행", "역사와 문화", "식도락 여행", "축제", "예술 및 체험", "산악 여행", "도심 속 여행", "바다와 해변", "테마파크"};
        String themesList = String.join(", ", themes);

        String prompt = String.format(
                "Title: %s\nLocation: %s\nDescription: %s\nTags: %s\n\nPlease respond with one of the following themes: %s",
                title, location, description, tags, themesList);

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo");
        request.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        request.put("max_tokens", 60);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        int retryCount = 0;
        int maxRetries = 5;
        long waitTime = 2000; // 2초

        while (retryCount < maxRetries) {
            try {
                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
                String theme = extractTheme(response);
                if (isValidTheme(theme, themes)) {
                    return theme;
                } else {
                    return "Invalid theme returned: " + theme;
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
                return "API 호출 실패: " + e.getMessage();
            }
        }
        return "API 호출 실패: 최대 재시도 횟수 초과";
    }

    private String extractTheme(ResponseEntity<Map> response) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        String theme = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
        return theme.trim();
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
