package com.SoT.JIN.password;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {

    // 비밀번호 재설정 폼을 보여주는 메서드
    @GetMapping("/password-reset")
    public String showPasswordResetForm(@RequestParam(name = "token", required = false) String token, Model model) {
        if (token == null || token.isEmpty()) {
            // 에러 메시지를 모델에 추가하여 에러 페이지로 이동
            model.addAttribute("error", "유효하지 않은 요청입니다. 토큰이 없습니다.");
            return "error";  // error.html 페이지로 이동
        }

        // 유효한 토큰이 있다면 폼에 토큰을 전달하여 비밀번호 재설정 페이지로 이동
        model.addAttribute("token", token);
        return "password-reset-form";  // password-reset-form.html 페이지로 이동
    }

    // 비밀번호 재설정 처리를 수행하는 메서드
    @PostMapping("/password-reset")
    public String handlePasswordReset(@RequestParam("token") String token,
                                      @RequestParam("newPassword") String newPassword,
                                      Model model) {
        if (token == null || token.isEmpty()) {
            model.addAttribute("error", "유효하지 않은 토큰입니다.");
            return "error";  // error.html 페이지로 이동
        }

        // TODO: 토큰 유효성 검사 및 비밀번호 재설정 로직 구현
        // 이 예제에서는 간단한 토큰 검사를 수행하고, 실제로는 서비스에서 처리
        if ("valid_token".equals(token)) {
            // 비밀번호 재설정 로직 수행
            model.addAttribute("message", "비밀번호가 성공적으로 재설정되었습니다.");
            return "password-reset-success";  // 성공 메시지 페이지로 이동
        } else {
            model.addAttribute("error", "유효하지 않은 토큰입니다.");
            return "error";  // error.html 페이지로 이동
        }
    }
}