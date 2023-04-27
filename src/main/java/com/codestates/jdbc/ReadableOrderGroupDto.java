package com.codestates.jdbc;

import com.codestates.order.entity.Order;
import com.codestates.jdbc.ReadableOrderCoffee;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode   // Stream으로 그룹핑을 할 때, 기준이 되는 키를 구분한다.
public class ReadableOrderGroupDto {
    private long orderId;
    private long memberId;
    private Order.OrderStatus orderStatus;
    private LocalDateTime createdAt;

    public ReadableOrderGroupDto(ReadableOrderCoffee readableOrderCoffee) {
        this.orderId = readableOrderCoffee.getOrderId();
        this.memberId = readableOrderCoffee.getMemberId();
        this.orderStatus = readableOrderCoffee.getOrderStatus();
        this.createdAt = readableOrderCoffee.getCreatedAt();
    }
}
