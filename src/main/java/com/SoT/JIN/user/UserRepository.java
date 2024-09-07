package com.SoT.JIN.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndVerificationCode(String phoneNumber, String verificationCode);

    @Query("SELECT u FROM User u JOIN u.followers f WHERE f.userId = :userId")
    List<User> findFollowingByUserId(@Param("userId") Long userId);

    // 전화번호가 존재하는지 확인하는 메서드
    boolean existsByPhoneNumber(String phoneNumber);

    // 이메일 중복 확인 메소드
    boolean existsByEmail(String email);

    // 닉네임 중복 확인 메소드
    boolean existsByUsername(String username);
}
