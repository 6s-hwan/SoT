package com.SoT.JIN.member;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long userId;
    private String email;
    private String password;
    private String username;
    private String birth;
    private String gender;
    private String phoneNumber;
    private String verificationCode; // 추가: 난수 저장 필드
}
