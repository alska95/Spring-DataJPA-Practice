package com.example.datajpa.repository;

import com.example.datajpa.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRespository memberJpaRespository;
    @Test
    public void paging() {
        //given
        Member member = new Member("hwang", 10);
        Member member1 = new Member("hwang", 20);
        Member member2 = new Member("hwang", 30);

        String name = "hwang";
        int offset = 0;
        int limit = 2;

        //when
        List<Member> members = memberJpaRespository.findByPage(name, offset, limit);
        long totalCount = memberJpaRespository.totalCount(name);

        //then
        Assertions.assertThat(members.size()).isEqualTo(2);


    }
}
