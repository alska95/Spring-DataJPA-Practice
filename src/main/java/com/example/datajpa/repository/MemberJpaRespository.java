package com.example.datajpa.repository;

import com.example.datajpa.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberJpaRespository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findByName(String name){
        return em.createNamedQuery("Member.findByName", Member.class)
                .setParameter("name", name)
                .getResultList();
        //구현 하는게 비효율 적이다! --> 편하게 호출하는 법이 있다.
    }
    public List<Member> findByPage(String name, int offset , int limit) { //offset부터 시작해서 limit개 만큼 가져오기
        return em.createQuery("select m from Member m where m.name =:name order by m.age asc", Member.class)
                .setParameter("name", name)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long totalCount(String name) {
        return em.createQuery("select count(m) from Member m where m.name =:name ", Long.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
