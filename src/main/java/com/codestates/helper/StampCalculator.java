package com.codestates.helper;

import com.codestates.order.entity.Order;
import com.codestates.order.entity.OrderCoffee;

public class StampCalculator {
    public static int calculateStampCount(int nowCount, int earned) {
        return nowCount + earned;
    }

    public static int calculateEarnedStampCount(Order order) {
        return order.getOrderCoffees()
                .stream()
                .mapToInt(OrderCoffee::getQuantity)
                .sum();
    }
}
