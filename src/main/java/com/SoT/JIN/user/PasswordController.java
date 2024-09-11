package com.SoT.JIN.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PasswordController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; // 이메일 전송 서비스
    private final PasswordResetTokenService tokenService; // 비밀번호 재설정 토큰 서비스
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordController(UserService userService, PasswordEncoder passwordEncoder, EmailService emailService, PasswordResetTokenService tokenService
    , PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request) {
        System.out.println("비밀번호 변경 요청 수신됨."); // 백엔드 로그 추가
        User currentUser = userService.getCurrentUser(); // 현재 로그인된 사용자 가져오기

        // 현재 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            return ResponseEntity.status(400).body("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 저장
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.updateUser(currentUser); // DB에 사용자 정보 업데이트

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    @Getter
    @Setter
    public static class PasswordChangeRequest {
        private String currentPassword;
        private String newPassword;

    }
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestParam("email") String email) {
        User user = userService.findByEmail(email);

        Map<String, String> response = new HashMap<>();

        // 사용자가 존재하지 않을 때의 처리
        if (user == null) {
            response.put("message", "해당 이메일이 존재하지 않습니다.");
            return ResponseEntity.status(400).body(response);
        }

// 이미 비밀번호 재설정 토큰이 있는지 확인
        PasswordResetToken existingToken = tokenService.findByUser(user);
        if (existingToken != null && !existingToken.isExpired()) {
            response.put("message", "이미 비밀번호 재설정 요청이 처리되었습니다. 이메일을 확인하세요.");
            return ResponseEntity.status(400).body(response);
        }

// 만료된 토큰이 있으면 삭제 또는 무효화
        if (existingToken != null && existingToken.isExpired()) {
            tokenService.invalidateToken(existingToken.getToken());  // 만료된 토큰 무효화
        }

        // 비밀번호 재설정 토큰 생성
        String token = UUID.randomUUID().toString();
        tokenService.createPasswordResetToken(user, token);

        // 비밀번호 재설정 링크 생성
        String resetLink = generatePasswordResetLink(token);

        // 이메일로 링크 전송
        emailService.sendEmail(user.getEmail(), "비밀번호 재설정 링크",
                "비밀번호를 재설정하려면 다음 링크를 클릭하세요: " + resetLink +
                        "\n이 링크는 30분 동안만 유효한 보안 토큰과 함께 동작합니다. 시간이 지나면 링크를 다시 요청해 주세요.");

        response.put("message", "비밀번호 재설정 링크가 이메일로 전송되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 비밀번호 재설정 링크 생성 메서드
    private String generatePasswordResetLink(String token) {
        // 실제 애플리케이션의 도메인으로 변경
        String appUrl = "http://storyoftravel.ap-northeast-2.elasticbeanstalk.com";
        return appUrl + "/reset-password?token=" + token; // 토큰을 쿼리 파라미터로 포함
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String token = request.getToken();
        String newPassword = request.getNewPassword();

        // 'email' 없이 토큰만으로 사용자 확인
        Optional<User> userOptional = tokenService.validatePasswordResetToken(token);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(400).body("유효하지 않은 토큰입니다. 비밀번호 재설정 요청을 다시 진행해 주세요.");
        }

        User user = userOptional.get();

        // 새 비밀번호 저장
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);

        // 사용된 토큰 삭제
        tokenService.invalidateToken(token);

        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }

    // 비밀번호 찾기 요청 데이터 클래스
    @Getter
    @Setter
    public static class ForgotPasswordRequest {
        private String email;
    }

    // 비밀번호 재설정 요청 데이터 클래스
    @Getter
    @Setter
    public static class ResetPasswordRequest {
        private String token;
        private String newPassword;
    }
}
