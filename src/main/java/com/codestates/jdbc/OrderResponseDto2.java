package com.codestates.jdbc;

import com.codestates.coffee.entity.Coffee;
import com.codestates.order.dto.OrderCoffeeResponseDto;
//import com.codestates.order.entity.CoffeeRef;
import com.codestates.coffee.service.CoffeeService;
import com.codestates.member.entity.Member;
import com.codestates.order.entity.Order;
import lombok.Getter;
import lombok.Setter;
//import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO에서 매핑을 처리하는 케이스를 보여주기 위한 OrderResponseDto2 클래스입니다.
 * 실제 사용되지는 않습니다.
 */
@Getter
@Setter
public class OrderResponseDto2 {
 /*   private long orderId;
    private long memberId;
    private Order.OrderStatus orderStatus;
    private List<OrderCoffeeResponseDto> orderCoffees;
    private LocalDateTime createdAt;

    // AggregateReference<Member, Long> memberId의 타입을 long 타입으로 변환해 주어야 한다.
    public void setMemberId(AggregateReference<Member, Long> memberId) {
        this.memberId = memberId.getId();
    }

    // Set<CoffeeRef> coffeeRefs 타입을 List<OrderCoffeeResponseDto> 타입으로 변환해 주어야 한다.
    public void setOrderCoffees(Set<CoffeeRef> coffeeRefs) {
        List<OrderCoffeeResponseDto> orderCoffees =
                coffeeRefs.stream()
                        .map(coffeeRef -> {
                            // DI를 할 수 없으므로, 직접 객체를 생성해줘야 한다.
                            Coffee coffee =
                                    new CoffeeService(null*//* CoffeeRepository 인스턴스를 넘겨야한다.*//*)
                                            .findCoffee(coffeeRef.getCoffeeId());

                            return new OrderCoffeeResponseDto(coffee.getCoffeeId(),
                                    coffee.getKorName(),
                                    coffee.getEngName(),
                                    coffee.getPrice(),
//                            coffee.getPrice().getValue(), // Money 타입을 사용할 경우
                                    coffeeRef.getQuantity());
                        }).collect(Collectors.toList());

        this.orderCoffees = orderCoffees;
    }*/
}
