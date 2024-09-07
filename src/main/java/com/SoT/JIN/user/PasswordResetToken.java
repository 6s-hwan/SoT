package com.SoT.JIN.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;
    // 만료 시간 계산
    public void setExpiryDate(int minutes) {
        this.expiryDate = LocalDateTime.now().plusMinutes(minutes);  // 예: 30분 후 만료
    }

    private LocalDateTime expiryDate; // 토큰 만료 시간
    // 토큰이 만료되었는지 확인하는 메서드 추가
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    // getter, setter 및 다른 필요한 메서드들
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}