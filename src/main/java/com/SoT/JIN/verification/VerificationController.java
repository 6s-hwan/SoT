package com.SoT.JIN.verification;

import com.SoT.JIN.user.User;
import com.SoT.JIN.user.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

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

    private static final long VERIFICATION_EXPIRY_TIME_MINUTES = 5; // 인증번호 유효시간 5분

    // 인증번호 생성 및 전송
    // 인증번호 전송 및 생성 시간 설정
    @PostMapping("/sendVerification")
    public ResponseEntity<String> sendVerification(@RequestBody VerificationRequest request) {
        String phoneNumber = request.getPhoneNumber();

        // 전화번호를 +82 형식으로 변환
        if (phoneNumber.startsWith("0")) {
            phoneNumber = "+82" + phoneNumber.substring(1);
        }

        // 이미 등록된 전화번호인지 확인 (User 테이블에서 확인)
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body("이미 가입된 전화번호입니다.");
        }

        String verificationCode = generateVerificationCode(); // 인증번호 생성

        try {
            // Twilio SMS 전송
            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    "[Story of Travel(SoT)] 인증번호는 [" + verificationCode + "]입니다. 5분 안에 입력해 주세요."
            ).create();

            System.out.println("SMS sent: " + message.getSid());

            // User 테이블에 인증번호와 생성 시간 저장 (기존 유저가 없으면 새로 생성)
            User user = userRepository.findByPhoneNumber(phoneNumber).orElse(new User());
            user.setPhoneNumber(phoneNumber);
            user.setVerificationCode(verificationCode);  // 인증번호 저장
            user.setVerificationCodeCreatedAt(LocalDateTime.now());  // 인증번호 생성 시간 저장
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

        // 전화번호 형식 검증 (예: +8210으로 시작하고, 뒤에 정확히 8자리 숫자가 붙는지 확인)
        if (!phoneNumber.matches("^\\+8210[0-9]{8}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("전화번호를 올바르게 입력해주세요.");
        }

        String verificationCode = generateVerificationCode();

        try {
            // Twilio API로 SMS 전송
            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    "[Story of Travel(SoT)] 인증번호는 [" + verificationCode + "]입니다. 5분 안에 입력해 주세요."
            ).create();

            System.out.println("SMS sent: " + message.getSid());

            // 이전 Verification 엔티티 삭제
            verificationRepository.deleteByPhoneNumber(phoneNumber);

            // 새 Verification 엔티티 생성 및 저장
            Verification verification = new Verification(phoneNumber, verificationCode);
            verificationRepository.save(verification);

            return ResponseEntity.ok("인증번호가 전송되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SMS 전송 중 오류가 발생했습니다.");
        }
    }
    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestBody VerificationRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String verificationCode = request.getVerificationCode();

        // 인증번호가 비어있는지 확인
        if (verificationCode == null || verificationCode.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증번호를 입력해 주세요.");
        }

        // 전화번호 형식 검증 (예: +8210으로 시작하고, 뒤에 정확히 8자리 숫자가 붙는지 확인)
        if (!phoneNumber.matches("^\\+8210[0-9]{8}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("전화번호를 올바르게 입력해주세요.");
        }

        // 인증 정보 확인
        Optional<Verification> optionalVerification = verificationRepository
                .findByPhoneNumberAndVerificationCode(phoneNumber, verificationCode);

        // 인증 정보가 존재하지 않으면
        if (optionalVerification.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증번호가 올바르지 않습니다.");
        }

        Verification verification = optionalVerification.get();

        // 인증번호 유효시간 검사
        LocalDateTime createdAt = verification.getCreatedAt();
        if (createdAt.isBefore(LocalDateTime.now().minusMinutes(VERIFICATION_EXPIRY_TIME_MINUTES))) {
            verificationRepository.delete(verification); // 인증번호 만료 시 삭제
            return ResponseEntity.status(HttpStatus.GONE).body("인증번호가 만료되었습니다. 다시 전송해 주세요.");
        }

        // 인증 성공 시 유저 정보 반환
        User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 번호로 가입된 이메일이 없습니다.");
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