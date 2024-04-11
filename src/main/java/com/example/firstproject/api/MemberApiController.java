package com.example.firstproject.api;

import com.example.firstproject.dto.MemberForm;
import com.example.firstproject.entity.Member;
import com.example.firstproject.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberApiController {
    @Autowired // 회원정보 repository 주입
    private MemberRepository memberRepository;
    // * GET
//    // 모든 회원정보 조회하기 - 구현 보류(일반적인 경우에는 필요없다고 함 GPT가..)
//    @GetMapping("/api/members")
//    public List<Member> index() {
//        return memberRepository.findAll();
//    }

    // 특정 회원정보 조회하기(이 방식이 일반적)
    @GetMapping("/api/members/{id}") // URL 요청 접수
    public Member show(@PathVariable Long id) { // @PathVariable: id를 요청 URL에서 가져오기 때문
        return memberRepository.findById(id).orElse(null);
    }


    // * POST
    // 회원정보를 생성해 목록에 저장
    @PostMapping("/api/members")
    public Member create(@RequestBody MemberForm dto) { // 수정할 데이터를 dto 매개변수로 받아옴
        // @RequestBody: 요청 시 본문(BODY)에 실어 보내는 데이터를 create()의 매개변수로 받아올 수 있음
        Member member = dto.toEntity(); // DB에서 사용할 수 있도록 엔티티로 변환해 member에 넣음
        return memberRepository.save(member); // memberRepository를 통해 DB에 저장한 후 반환
    }
    
    // * PATCH
    // * DELETE
}