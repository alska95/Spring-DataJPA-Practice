package com.example.datajpa.repository;

import com.example.datajpa.domain.Member;
import com.example.datajpa.domain.Team;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class MemberSpec {
    public static Specification<Member> teamName(final String teamName){
        return new Specification<Member>(){

            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Member, Team> team = root.join("team", JoinType.INNER);

                return criteriaBuilder.equal(team.get("name"), teamName);
            }
        };
    } //참고만 하자...

}
