package com.example.datajpa.repository;

import com.example.datajpa.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MemberRespositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Test
    public void testMember(){
        Member member = new Member("hwang");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(savedMember).isEqualTo(findMember);
    }
}