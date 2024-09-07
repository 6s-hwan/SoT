package com.SoT.JIN.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserService userService;

    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository, UserService userService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    // 비밀번호 재설정 토큰 생성 및 저장
    public void createPasswordResetToken(User user, String token) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // 30분 유효기간
        tokenRepository.save(resetToken);
    }

    // 토큰 유효성 검사
    public Optional<User> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);

        if (tokenOptional.isPresent()) {
            PasswordResetToken resetToken = tokenOptional.get();

            // 토큰 만료 검사
            if (resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                return Optional.of(resetToken.getUser());
            }
        }

        return Optional.empty(); // 토큰이 없거나 만료된 경우
    }

    @Transactional // 트랜잭션 처리 필요
    public void invalidateToken(String token) {
        tokenRepository.deleteByToken(token);
    }
    public PasswordResetToken findByUser(User user) {
        return tokenRepository.findByUser(user);
    }
}
