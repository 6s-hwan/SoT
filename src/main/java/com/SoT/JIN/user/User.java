package com.SoT.JIN.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
    private String username;
    private String birth;
    private String gender;
    private String phoneNumber;
    private String verificationCode;
    private String profileImageUrl; // 추가: 프로필 이미지 URL
    // 추가: 인증번호 생성 시간
    private LocalDateTime verificationCodeCreatedAt;
    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

    private int followCount;

    public void follow(User user) {
        this.following.add(user);
        user.getFollowers().add(this);
    }

    public void unfollow(User user) {
        this.following.remove(user);
        user.getFollowers().remove(this);
    }
    // 인증번호 생성 시간을 설정하는 메서드
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        this.verificationCodeCreatedAt = LocalDateTime.now(); // 인증번호 생성 시간 설정
    }
}
