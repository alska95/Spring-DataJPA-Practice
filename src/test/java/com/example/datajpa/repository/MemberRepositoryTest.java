package com.example.datajpa.repository;

import com.example.datajpa.domain.Member;
import com.example.datajpa.domain.Team;
import com.example.datajpa.repository.projection.ClosedProjections;
import com.example.datajpa.repository.projection.NameOnly;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@SpringBootTest
class MemberRepositoryTest {
    
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberJpaRespository memberJpaRespository;
    @Autowired
    private TeamRepository teamRepository;

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

    @Test
    @Rollback
    public void findMemberLazy(){
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("hwang", 10 , teamA);
        Member member2 = new Member("hwang2", 210 , teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        
        em.flush();
        em.clear();
        
        //when
        List<Member> members = memberRepository.findAll();
        for(Member member : members){
            System.out.println("member.getName() = " + member.getName()); //1번의 쿼리 리스트 가져올때
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); //프록시 가져옴(fetch = FetchType.lazy 이기 때문에
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName()); //n번의 쿼리
            //팀을 가져오고 걔의 요소에 접근 할 때, 쿼리를 다시 돌려서 가져와야 됨.
            //n+1은 lazy로 바꾼다고 해결되는게 아니라, 잠시 미룰 뿐임. eager일땐 항상 저만큼 연산이 일어남.
        }
    }

    @Test
    @Rollback
    public void findMemberFetch(){
        /*
        * select member0_.member_id as member_i1_2_0_,
        *  team1_.team_id as team_id1_4_1_,
        * member0_.city as city2_2_0_,
        * member0_.street as street3_2_0_,
        *  member0_.zipcode as zipcode4_2_0_,
        *  member0_.age as age5_2_0_,
        * member0_.name as name6_2_0_,
        * member0_.team_id as team_id7_2_0_,
        * team1_.name as name2_4_1_
        * from members member0_
        * left outer join team team1_
        *  on member0_.team_id=team1_.team_id
        *  한번에 다 끌고 온다!*/
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("hwang", 10 , teamA);
        Member member2 = new Member("hwang2", 210 , teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findAll();
        for(Member member : members){
            System.out.println("member.getName() = " + member.getName()); //1번의 쿼리 리스트 가져올때
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); //프록시 가져옴(fetch = FetchType.lazy 이기 때문에
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName()); //n번의 쿼리
            //팀을 가져오고 걔의 요소에 접근 할 때, 쿼리를 다시 돌려서 가져와야 됨.
            //n+1은 lazy로 바꾼다고 해결되는게 아니라, 잠시 미룰 뿐임. eager일땐 항상 저만큼 연산이 일어남.
        }
    }
    @Test
    public void queryHint(){
        memberRepository.save(new Member("member1" , 10));
        em.flush(); //쿼리는 날라가고, 영속성 컨텍스트는 유지된다.
        em.clear(); //영속성 컨텍스트까지 날라간다.

        Member findMember = memberRepository.findByName("member1").get(0);
        findMember.setName("hwang"); //변경 감지로 commit시에 쿼리가 날라감. Dirtycheck등에 비용이 들어간다.
        em.flush(); //

        Member findOnlyMember = memberRepository.findReadOnlyByName("hwang");
        findOnlyMember.setName("kyeong"); //변경감지 체크를 안한다. --> 효율적임.
    }

    @Test
    public void lock(){
        //given
        Member member1 = new Member("member1" , 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member member11 = memberRepository.findLockByName("member1");
        /*
        * select * from members member0_ where member0_.name=?
        * for update
        * db에서 락을 걸어버림.
        * 대안으로 JPA에서 제공하는 @Version 같은 optimistic lock이 있음.
        * */
        member11.setName("hwang");
        em.flush();
    }

    @Test
    public void callCustom(){
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }
    
    @Test
    public void JpaEventBaseEntity() throws Exception{
        //given
        Member member = new Member("member1");
        memberRepository.save(member); //@PrePersist발 생
        Thread.sleep(100);
        member.setName("hwang");
        em.flush();
        em.clear();
        //when
        Member findMember = memberRepository.findByName("hwang").get(0);
        //then
        System.out.println("findMember.getCreatedDate = " + findMember.getCreatedDate());
        System.out.println("findMember.getUpdateDate() = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());
    }

    @Test
    public void queryByExample(){

        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1",0,teamA);
        Member m2 = new Member("m2",0,teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Member member = new Member("m1");
        member.setTeam(teamA); //inner join까진 가능.

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(example);// QueryByExampleExecutor example 받아준다.


    }

    @Test
    public void projections(){

        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1",0,teamA);
        Member m2 = new Member("m2",0,teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<NameOnly> m11 = memberRepository.findProjectionByName("m1");
        for (NameOnly nameOnly : m11) {
            System.out.println("nameOnly = " + nameOnly);
        }

        List<ClosedProjections> closedResult = memberRepository.findProjection3ByName("m1", ClosedProjections.class);
        for (ClosedProjections closedProjections : closedResult) {
            System.out.println("closedProjections.getName() = " + closedProjections.getName());
            System.out.println("closedProjections.getTeam() = " + closedProjections.getTeam());
        }
    }
//스프링DATAJPA가 알아서 인식해서 name만 필요함을 인지하고 해당하는 구현체를 구현해준다.


}
