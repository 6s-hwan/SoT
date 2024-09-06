package com.SoT.JIN.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);

    void deleteByUser(User user); // 사용자를 기반으로 토큰 삭제
}
