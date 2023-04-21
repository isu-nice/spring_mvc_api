package com.codestates.coffee.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Coffee {

    @Id
    private long coffeeId;
    private String korName;
    private String engName;
    private Integer price;
    private String coffeeCode;
}
