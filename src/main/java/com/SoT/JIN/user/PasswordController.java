package com.SoT.JIN.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PasswordController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
}
