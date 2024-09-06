package com.SoT.JIN.user;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// jakarta.mail 패키지 사용
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 이메일 전송 메서드
    public void sendEmail(String to, String subject, String text) {
        try {
            // MimeMessage 객체 생성 (jakarta.mail로 변경)
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setTo(to); // 수신자 이메일
            helper.setSubject(subject); // 이메일 제목
            helper.setText(text, true); // true일 경우 HTML을 사용한 메일 가능
            helper.setFrom("noreply@yourapp.com"); // 발신자 이메일

            // 이메일 전송
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 중 오류가 발생했습니다.", e);
        }
    }
}
