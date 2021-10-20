package com.example.datajpa.controller;

import com.example.datajpa.domain.Member;
import com.example.datajpa.dto.MemberDto;
import com.example.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getName();
    }

    @GetMapping("/members2/{id}") //도메인 클래스 컨버터 사용
    public String findMember2(@PathVariable("id") Member member){
        return member.getName();
    }
/*
* 스프링이 컨버팅 과정 다 끝내고 인잭션을 해준다. 권장하지는 않는다. 조회용으로만 사용할것.*/

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 3) Pageable pageable){
        Page<Member> paged = memberRepository.findAll(pageable);
        Page<MemberDto> mapped = paged.map(v -> new MemberDto(v.getName()));
        return mapped;
    }
    /*members?page=0&size=3&sort=id,desc 과 같이 요청한다
    * default pageSize는 20*/

    @PostConstruct
    public void init(){
        memberRepository.save(new Member("userA"));
        for(int i = 0 ; i < 100 ; i++){
            memberRepository.save(new Member("user"+i));
        }
    }

}
