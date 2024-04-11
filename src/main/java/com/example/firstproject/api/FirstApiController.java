package com.example.firstproject.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // REST API용 컨트롤러
public class FirstApiController {
    @GetMapping("/api/hello") // URL 요청 접수
    public String hello() { // hello world! 문자열 반환
        return "hello world!";
    }
}
