package com.SoT.JIN.verification;

import com.SoT.JIN.user.User;
import com.SoT.JIN.user.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class VerificationController {

    @Value("${twilio.account.sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationRepository verificationRepository;


    @PostMapping("/sendVerification")
    public ResponseEntity<String> sendVerification(@RequestBody VerificationRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String verificationCode = generateVerificationCode(); // 인증번호 생성

        try {
            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    "Your verification code is: " + verificationCode
            ).create();

            System.out.println("SMS sent: " + message.getSid());

            // 사용자가 있는지 확인하고, 없으면 새로 생성하여 인증번호 저장
            User user = userRepository.findByPhoneNumber(phoneNumber).orElseGet(User::new);
            user.setPhoneNumber(phoneNumber);
            user.setVerificationCode(verificationCode);
            userRepository.save(user);

            return ResponseEntity.ok("인증번호가 전송되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("SMS 전송 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    @PostMapping("/sendVerificationCode")
    public ResponseEntity<String> sendVerificationCode(@RequestBody VerificationRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String verificationCode = generateVerificationCode();

        try {
            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    "Your verification code is: " + verificationCode
            ).create();

            System.out.println("SMS sent: " + message.getSid());

            // 이전 Verification 엔티티 삭제
            verificationRepository.deleteByPhoneNumber(phoneNumber);

            // Verification 엔티티에 인증번호 저장
            Verification verification = new Verification(phoneNumber, verificationCode);
            verificationRepository.save(verification);

            return ResponseEntity.ok("인증번호가 전송되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("SMS 전송 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestBody VerificationRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String verificationCode = request.getVerificationCode();

        // 인증 정보 확인
        Verification verification = verificationRepository
                .findByPhoneNumberAndVerificationCode(phoneNumber, verificationCode)
                .orElse(null);

        if (verification == null || verification.getCreatedAt().isBefore(LocalDateTime.now().minusHours(1))) {
            return ResponseEntity.status(400).body("인증번호가 잘못되었거나 만료되었습니다.");
        }

        // 인증 성공 시 유저 정보 반환
        User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }

        // 인증 정보 삭제
        verificationRepository.delete(verification);

        return ResponseEntity.ok(user.getEmail());
    }
    private String generateVerificationCode() {
        // 6자리 숫자로 인증번호 생성
        return String.valueOf((int) ((Math.random() * (999999 - 100000)) + 100000));
    }
}