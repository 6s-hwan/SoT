package com.SoT.JIN.password;

import com.SoT.JIN.password.PasswordResetToken;
import com.SoT.JIN.password.PasswordResetTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class PasswordResetFormController {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @GetMapping("/password-reset")
    public String showPasswordResetForm(@RequestParam String token) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);

        if (resetToken.isPresent() && resetToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            // 유효한 토큰이므로 비밀번호 재설정 폼을 보여줌
            return "password-reset-form";
        } else {
            // 토큰이 유효하지 않음
            return "invalid-token";
        }
    }

    @PostMapping("/password-reset")
    public String handlePasswordReset(@RequestParam String token, @RequestParam String newPassword) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);

        if (resetToken.isPresent() && resetToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            // 비밀번호 재설정 로직 (이메일로 사용자 찾아서 비밀번호 변경)
            // 예: userService.updatePassword(resetToken.get().getUserEmail(), newPassword);

            // 토큰 사용 완료로 삭제
            tokenRepository.delete(resetToken.get());

            return "password-reset-success";
        } else {
            return "invalid-token";
        }
    }
}
