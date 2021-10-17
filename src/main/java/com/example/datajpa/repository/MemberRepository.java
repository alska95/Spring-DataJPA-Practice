package com.example.datajpa.repository;

import com.example.datajpa.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {//첫번째 타입, 두번째 매핑된 pk

    /*
    * JpaRepository<T , ID> 타입과 PK
    * spring data 프로젝트가 공통된 crud를 제공하고, jpaRepository는 jpa특화 기능을 제공한다.
    * (paging, sorting같은 공통적인건 crudRepository가 제공한다는뜻)
    * crudRepository : 기본적인거 findBy(~) 등 기타등등 메소드들이 들어있다.
    * 최상위 단
    * Repository<T,ID> spring bean을 만들때 classPath를 쉽게 찾을 수 있도록 만들어준다.
    *
    * Repository <- crudRepository <- pagingAndSortingRepository <- JpaRepository
    *
    *
    * */

}
