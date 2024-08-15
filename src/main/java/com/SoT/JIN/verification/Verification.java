package com.SoT.JIN.verification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "verification", uniqueConstraints = {@UniqueConstraint(columnNames = {"phoneNumber", "verificationCode"})})
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;
    private String verificationCode;
    private LocalDateTime createdAt;

    // Constructor, Getter, Setter

    public Verification() {}

    public Verification(String phoneNumber, String verificationCode) {
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
}
