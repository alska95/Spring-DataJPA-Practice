package com.example.datajpa.repository.projection;

import lombok.Getter;

@Getter
public class UsernameOnlyDto {

    private final String name;

    public UsernameOnlyDto(String name){ //파라미터 명을 보고 분석한다!
        this.name =name;
    }
}
