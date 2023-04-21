package com.codestates.member.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Member {

    @Id
    private long memberId;
    private String email;
    private String name;
    private String phone;
}
