package com.example.datajpa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@AllArgsConstructor
@Data
@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name ="team_id")
    private Long id;

    private String name;
    public Team(String name){
        this.name = name;
    }
    public Team() {
    }
}
