package com.codestates.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity postOrder(
            @RequestParam("memberId") long memberId,
            @RequestParam("coffeeId") long coffeeId
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("coffeeId", coffeeId);

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity getOrder(@PathVariable("order-id") long orderId) {
        System.out.println("# orderId = " + orderId);

        // not implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getOrders() {
        System.out.println("# get Orders");

        // not implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
