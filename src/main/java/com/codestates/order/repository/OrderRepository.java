package com.codestates.order.repository;

import com.codestates.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jdbc.repository.query.Query;
// import org.springframework.data.repository.CrudRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
   /* @Query("SELECT O.*, OC.QUANTITY, C.* FROM ORDERS O " +
            "INNER JOIN ORDER_COFFEE OC " +
            "ON O.ORDER_ID = OC.ORDER_ID " +
            "INNER JOIN COFFEE C " +
            "ON OC.COFFEE_ID = C.COFFEE_ID " +
            "ORDER BY O.ORDER_ID DESC"
    )
    List<ReadableOrderCoffee> findAllOrderCoffee();
    */
}
