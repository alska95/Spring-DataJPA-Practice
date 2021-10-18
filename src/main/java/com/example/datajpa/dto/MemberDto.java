package com.example.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MemberDto {
    private Long id;
    private String userMame;
    private String teamName;
}
