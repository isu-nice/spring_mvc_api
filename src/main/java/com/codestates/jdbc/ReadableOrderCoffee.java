package com.codestates.jdbc;

import com.codestates.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReadableOrderCoffee {
    private long orderId;
    private long memberId;
    private Order.OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private long coffeeId;
    private int quantity;
    private String korName;
    private String engName;
    private int price;
    private String coffeeCode;
}