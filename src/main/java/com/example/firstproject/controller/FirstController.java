package com.example.firstproject.controller;

import org.springframework.stereotype.Controller;//컨트롤러 선언과 동시에 자동으로 임포트
import org.springframework.ui.Model;//Model 선언과 동시 임포트
import org.springframework.web.bind.annotation.GetMapping;//URL 연결 요청과 동시에 임포트

@Controller
public class FirstController {
    @GetMapping("/hi")
    public String niceToMeetYou(Model model){//model 객체 받아오기
        model.addAttribute("username","sujin");
        return "greetings";
    }
    @GetMapping("/bye")
    public String SeeYouNext(Model model){
        model.addAttribute("nickname","sujin");
        return "goodbye";
    }
}
