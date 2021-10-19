package com.example.datajpa.repository;

import com.example.datajpa.domain.Member;
import org.apache.logging.slf4j.SLF4JLogger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRepositoryTest {
    
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberJpaRespository memberJpaRespository;

    @PersistenceContext
    private EntityManager em;
    @Test
    public void testMember(){
        
        Member member = new Member("hwang");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(savedMember).isEqualTo(findMember);
    }

    @Test
    public void testQueryMember(){
        Member member = new Member("hwang" , 10);
        Member member1 = new Member("hwang" , 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        List<Member> hwang20 = memberRepository.findByNameAndAgeGreaterThan("hwang", 10);
        Assertions.assertThat(hwang20.get(0).getName()).isEqualTo("hwang");

        memberRepository.findTop3ABy();
    }
    @Test
    public void testNamedQueryMember(){
        Member member = new Member("hwang" , 10);
        memberRepository.save(member);
        List<Member> hwang = memberJpaRespository.findByName("hwang");

        Assertions.assertThat(hwang.get(0).getName()).isEqualTo("hwang");
    }
    @Test
    public void testNamedQueryMember2(){
        Member member = new Member("hwang" , 10);
        memberRepository.save(member);
        List<Member> hwang = memberRepository.findByName("hwang");

        Assertions.assertThat(hwang.get(0).getName()).isEqualTo("hwang");
    }

    @Test
    public void testQueryInRepository(){
        Member member = new Member("hwang" , 10);
        memberRepository.save(member);
        List<Member> hwang = memberRepository.findUser("hwang" , 10);

        Assertions.assertThat(hwang.get(0).getName()).isEqualTo("hwang");
    }

    @Test
    public void findNameQueryInRepository(){
        Member member = new Member("hwang" , 10);
        memberRepository.save(member);
        List<String> nameList = memberRepository.findNameList();
        for (String s : nameList) {
            System.out.println(s);
        }
    }

    @Test
    public void returntype(){

        Member member = new Member("hwang" , 10);
        Member member1 = new Member("hwang" , 20);
        Member member2 = new Member("hwang" , 30);
        memberRepository.save(member);
        memberRepository.save(member1);
        memberRepository.save(member2);

//        Optional<Member> hwang20 = memberRepository.findOneByName("hwang"); NonUniqueResultException을 발생시킨다!
//        Assertions.assertThat(hwang20.get().getName()).isEqualTo("hwang");

        memberRepository.findTop3ABy();
    }


    @Test
    public void paging() {
        //given
        Member member = new Member("hwang", 10);
        Member member1 = new Member("hwang", 20);
        Member member2 = new Member("hwang", 30);
        memberRepository.save(member);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        PageRequest page = PageRequest.of(0, 2, Sort.Direction.DESC, "name");//0페이지에서 2개만큼 Sort.Direction.Desc로, "name"으로 정렬해서 가져와
        String name = "hwang";
        int offset = 0;
        int limit = 2;
        Page<Member> byAge = memberRepository.findByAge(20, page);

        //then
        System.out.println("byAge.hasNext() = " + byAge.hasNext());
        System.out.println("byAge.getNumberOfElements() = " + byAge.getNumberOfElements());


        Assertions.assertThat(page.getPageSize()).isEqualTo(2);
    }

    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member1", 20));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member2", 30));
        memberRepository.save(new Member("member2", 30));

        em.flush(); //변경내용 반영
        em.clear();

        //when
        int i = memberRepository.bulkAgePlus(29);

        List<Member> member3 = memberRepository.findByName("member3");
        System.out.println("member3 = " + member3.get(0).getAge()); //벌크 연산은 영속성 컨텍스트를 사용하지 않기때문에, member3에는 적용되지 않은 모습이다.
        //then
        System.out.println(i);

    }
}