package com.codestates.order.controller;

import com.codestates.dto.MultiResponseDto;
import com.codestates.dto.SingleResponseDto;
import com.codestates.member.entity.Member;
import com.codestates.member.service.MemberService;
import com.codestates.order.dto.OrderPatchDto;
import com.codestates.order.dto.OrderPostDto;
import com.codestates.order.entity.Order;
import com.codestates.order.mapper.OrderMapper;
import com.codestates.order.service.OrderService;
import com.codestates.stamp.Stamp;
import com.codestates.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/v11/orders")
@Validated
public class OrderController {
    // Default URL 경로
    private final static String ORDER_DEFAULT_URL = "/v11/orders";

    private final OrderService orderService;
    private final MemberService memberService;
    private final OrderMapper mapper;

    public OrderController(OrderService orderService,
                           OrderMapper mapper,
                           MemberService memberService) {
        this.orderService = orderService;
        this.mapper = mapper;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity postOrder(@Valid @RequestBody OrderPostDto orderPostDto) {
        Order order = orderService.createOrder(mapper.orderPostDtoToOrder(orderPostDto));


        // "/v10/orders/{order-id}"
        URI location = UriCreator.createUri(ORDER_DEFAULT_URL, order.getOrderId());

        return ResponseEntity.created(location).build(); // HTTP 201 Created status
    }

    private void updateStamp(Order order) {
        Member member = memberService.findMember(order.getMember().getMemberId());
        Stamp stamp = member.getStamp();

        int existingStamps = stamp.getStampCount();
        int coffeeCount = order.getOrderCoffees()
                .stream()
                .mapToInt(orderCoffee -> orderCoffee.getQuantity())
                .sum();

        stamp.setStampCount(existingStamps + coffeeCount);
        member.setStamp(stamp);

        memberService.updateMember(member);
    }

    @PatchMapping("/{order-id}")
    public ResponseEntity patchOrder(@PathVariable("order-id") @Positive long orderId,
                                     @Valid @RequestBody OrderPatchDto orderPatchDto) {

        orderPatchDto.setOrderId(orderId);
        Order order = orderService.updateOrder(mapper.orderPatchDtoToOrder(orderPatchDto));

        return new ResponseEntity<>(new SingleResponseDto<>(
                mapper.orderToOrderResponseDto(order)), HttpStatus.OK);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity getOrder(@PathVariable("order-id") @Positive long orderId) {
        Order order = orderService.findOrder(orderId);

        // 주문한 커피 정보를 가져오도록 수정
        return new ResponseEntity<>(new SingleResponseDto<>(
                mapper.orderToOrderResponseDto(order)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getOrders(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size) {
        Page<Order> pageOrders = orderService.findOrders(page - 1, size);
        List<Order> orders = pageOrders.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(
                mapper.ordersToOrderResponseDtos(orders), pageOrders), HttpStatus.OK);
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity cancelOrder(@PathVariable("order-id") long orderId) {
        orderService.cancelOrder(orderId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
