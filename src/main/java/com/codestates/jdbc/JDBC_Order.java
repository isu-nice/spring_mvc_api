package com.codestates.jdbc;

// JDBC 학습 이후 폴더 변경함 -> 사용하지 않음
public class JDBC_Order {

}

/*
import com.codestates.member.entity.Member;

import com.codestates.order.entity.CoffeeRef;
import com.codestates.order.entity.Order;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Table("ORDERS")
public class JDBC_Order {
    @Id
    private long orderId;

    // 테이블 외래키처럼 memberId를 추가해서 참조하도록 한다.
    private AggregateReference<Member, Long> memberId;

    /*
     * CoffeeRef(ORDER_COFFEE 테이블)의 연결 설정을 여기서 해준다고 생각하자.
     * 그러므로 ORDER_COFFEE 테이블의 FK는 ORDER_ID, PK는 ORDER_COFFEE_ID

     * idColumn(FK): 자식 테이블에 추가되는 외래키에 해당되는 열명
      ORDERS 테이블의 자식 테이블은 ORDER_COFFEE 테이블이고,
      이 ORDER_COFFEE 테이블은 ORDERS 테이블의 PK인 ORDER_ID 열이 FK가 된다.

     * keyColumn(PK): 외래키를 포함하고 있는 테이블의 기본키 열명
      ORDERS 테이블의 자식 테이블인 ORDER_COFFEE 테이블의 PK는 ORDER_COFFEE_ID 이므로,
      ORDER_COFFEE_ID 열이 PK가 된다.
     */
/*
    @MappedCollection(idColumn = "ORDER_ID", keyColumn = "ORDER_COFFEE_ID")
    private Set<CoffeeRef> orderCoffees = new LinkedHashSet<>();

    private Order.OrderStatus orderStatus = Order.OrderStatus.ORDER_REQUEST;
    private LocalDateTime createdAt = LocalDateTime.now();

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
*/

