package com.example.firstproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 id 자동 생성
    Long id;

    @Column
    String email;

    @Column
    String password;

    @Column
    String name;

    // 생년월일
    @Column
    Integer birthYear; // 출생 Year

    @Column
    Integer birthMonth; // 출생 Month

    @Column
    Integer birthDay; // 출생 Day

    // 전화번호 필요할지?
    @Column
    String phone; // 전화번호

    @Column
    Boolean gender; // 성별 (남성: true, 여성: false)

    // 지역
    @Column
    String region1; // 시/도

    @Column
    String region2; // 시/구/군

    @Column
    String region3; // 동/읍/면

    public Member(Long id, String email, String password) {
    }
}