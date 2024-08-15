package com.SoT.JIN.verification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByPhoneNumberAndVerificationCode(String phoneNumber, String verificationCode);
    void deleteByPhoneNumber(String phoneNumber);
}