package com.SoT.JIN.verification;

import com.SoT.JIN.user.User;
import com.SoT.JIN.user.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/sendVerification")
    public ResponseEntity<String> sendVerificationCode(@RequestBody VerificationRequest request) {
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

    private String generateVerificationCode() {
        // 6자리 숫자로 인증번호 생성
        return String.valueOf((int) ((Math.random() * (999999 - 100000)) + 100000));
    }
}