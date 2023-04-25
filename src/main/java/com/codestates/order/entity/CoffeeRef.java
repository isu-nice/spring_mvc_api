package com.codestates.order.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@Table("ORDER_COFFEE")
public class CoffeeRef { // 주문된 커피
    @Id
    private long orderCoffeeId;

    private long coffeeId;
    private int quantity;
}
