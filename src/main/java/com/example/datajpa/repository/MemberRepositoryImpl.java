package com.example.datajpa.repository;

import com.example.datajpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
