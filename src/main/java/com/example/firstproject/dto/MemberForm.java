package com.example.firstproject.dto;

import com.example.firstproject.entity.Member;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
public class MemberForm {
    // phone
    // id, email, password, name, birthYear, birthMonth, birthDay, gender, region1, region2, region3
    private Long id;
    private String email; // 이메일 - O
    private String password; // 비밀번호 - O
    private String name; // 이름(닉네임)
    private Integer birthYear; // 출생 Year
    private Integer birthMonth; // 출생 Month
    private Integer birthDay; // 출생 Day
    private String phone; // 전화번호 필요할지?
    private Boolean gender; // 성별 (남성: true, 여성: false)
    private String region1; // 시/도
    private String region2; // 시/구/군
    private String region3; // 동/읍/면

//    public Member toEntity() {
//        return new Member(id, email, password);
//    }
    public Member toEntity() {
        return new Member(id, email, password, name, birthYear, birthMonth, birthDay, phone, gender, region1, region2, region3);
    }

}