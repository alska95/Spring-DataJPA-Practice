package com.example.datajpa.repository;

import com.example.datajpa.domain.Member;
import com.example.datajpa.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    List<Member> findByNameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3ABy();

//    @Query(name = "Member.findByName")
    List<Member> findByName(@Param("name") String name);

    @Query("select m from Member m where m.name = :name and m.age = :age")
    List<Member> findUser(@Param("name") String name, @Param("age") int age);

    @Query("select m.name from Member m")
    List<String> findNameList();

    @Query("select new com.example.datajpa.dto.MemberDto(m.id, m.name, o.name) from Member m join m.orders o")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.name in :names")
    List<Member> findByNames(@Param("names") List<String> names);


    @Query("select m from Member m where m.name in :names")
    Optional<Member> findOneByName(@Param("names") String names);
}
