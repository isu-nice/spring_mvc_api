package com.codestates.jdbc;

import com.codestates.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
//import org.springframework.data.jdbc.core.mapping.AggregateReference;
//import org.springframework.data.relational.core.mapping.MappedCollection;
//import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entity에서 매핑을 처리하는 케이스를 보여주기 위한 Order2 클래스입니다.
 * 실제 사용되지는 않습니다.
 */
@Getter
@Setter
//@Table("ORDERS")
public class Order2 {
    @Id
    private long orderId;

//    // 테이블 외래키처럼 memberId를 추가해서 참조하도록 한다..
//    private AggregateReference<Member, Long> memberId;
//
//    @MappedCollection(idColumn = "ORDER_ID", keyColumn = "ORDER_COFFEE_ID")
//    private Set<CoffeeRef> orderCoffees = new LinkedHashSet<>();
//
//    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST;
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    // Entity에서 매핑을 처리하는 케이스
//    public void setMemberId(long memberId) {
//        this.memberId = new AggregateReference.IdOnlyAggregateReference(memberId);
//    }

//    // Entity에서 매핑을 처리하는 케이스
//    public void setOrderCoffees(List<CoffeeRef> orderCoffees) {
//        Set<CoffeeRef> convertedOrderCoffees =
//                orderCoffees
//                        .stream()
//                        .map(orderCoffeeDto ->
//                                CoffeeRef.builder()
//                                        .coffeeId(orderCoffeeDto.getCoffeeId())
//                                        .quantity(orderCoffeeDto.getQuantity())
//                                        .build())
//                        .collect(Collectors.toSet());
//
//        this.orderCoffees = convertedOrderCoffees;
//    }

    public enum OrderStatus {
        ORDER_REQUEST(1, "주문 요청"),
        ORDER_CONFIRM(2, "주문 확정"),
        ORDER_COMPLETE(3, "주문 완료"),
        ORDER_CANCEL(4, "주문 취소");

        @Getter
        private int stepNumber;

        @Getter
        private String stepDescription;

        OrderStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }
}