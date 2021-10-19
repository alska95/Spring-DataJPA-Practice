package com.example.datajpa.repository;

import com.example.datajpa.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
