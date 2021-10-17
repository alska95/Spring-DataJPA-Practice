package com.example.datajpa.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "members")
@Data
@AllArgsConstructor
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;
    @Embedded
    private Address address;
    private int age;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member(String name){
        this.name = name;
    }
    public Member(String name, int age){
        this.name = name;
        this.age = age;
    }

    protected Member(){

    } //스팩상 protected로 -->JPA구현체들이 객체를 만들어낼때 사용하기 위함.

}
