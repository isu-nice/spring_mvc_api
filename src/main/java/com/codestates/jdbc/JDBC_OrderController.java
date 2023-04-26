package com.codestates.jdbc;

import com.codestates.dto.MultiResponseDto;
import com.codestates.dto.SingleResponseDto;
import com.codestates.jdbc.ReadableOrderGroupDto;
import com.codestates.member.service.MemberService;
import com.codestates.order.dto.*;
import com.codestates.order.entity.Order;
import com.codestates.jdbc.ReadableOrderCoffee;
import com.codestates.order.mapper.OrderMapper;
import com.codestates.order.service.OrderService;
import com.codestates.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@RestController
//@RequestMapping(value = "/v11/orders")
//@Validated
public class JDBC_OrderController {
/*    // Default URL 경로
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
        return new ResponseEntity<>(mapper.orderToOrderResponseDto(coffeeService, order),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getOrders(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size) {
        Page<Order> pageOrders = orderService.findOrders(page - 1, size);
        List<Order> orders = pageOrders.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.ordersToOrderResponseDtos(orders)),
                HttpStatus.OK);
    }

    *//**
     * N + 1 이슈가 없는 개선된 버전의 주문 목록 조회
     *//*
    // Step 1: 네이티브 쿼리로 Join된 주문한 커피 정보
    @GetMapping("/no-nplus1/v1")
    public ResponseEntity getOrdersNoNPlus1V1() {
        List<ReadableOrderCoffee> orders = orderService.findOrders2();

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Step 2: 주문한 커피별로 그룹핑하기
    @GetMapping("/no-nplus1/v2")
    public ResponseEntity getOrdersNoNPlus1V2() {
        List<ReadableOrderCoffee> orders = orderService.findOrders2();


        return new ResponseEntity<>(orders.stream()
                .collect(Collectors.groupingBy(ReadableOrderCoffee::getOrderId,
                        Collectors.groupingBy(ReadableOrderCoffee::getMemberId))), HttpStatus.OK);
    }

    // Step 3: 그룹핑된 주문한 커피 정보를 우리가 원하는 데이터 형식으로 변환하기
    @GetMapping("/no-nplus1/v3")
    public ResponseEntity getOrdersNoNPlus1V3() {
        List<ReadableOrderCoffee> orders = orderService.findOrders2();

        Map<ReadableOrderGroupDto, List<ReadableOrderCoffee>> grouped =
                orders.stream().collect(
                        Collectors.groupingBy(readableOrderCoffee -> new ReadableOrderGroupDto(readableOrderCoffee)));

        List<OrderResponseDto> response = grouped.entrySet().stream()
                .map(e -> {
                    ReadableOrderGroupDto groupDto = e.getKey();
                    List<ReadableOrderCoffee> readableOrderCoffees = e.getValue();
                    OrderResponseDto orderResponseDto = new OrderResponseDto();
                    orderResponseDto.setOrderId(groupDto.getOrderId());
                    orderResponseDto.setMemberId(groupDto.getMemberId());
                    orderResponseDto.setOrderStatus(groupDto.getOrderStatus());
                    orderResponseDto.setCreatedAt(groupDto.getCreatedAt());

                    List<OrderCoffeeResponseDto> orderCoffeeResponseDtos =
                            readableOrderCoffees.stream()
                                    .map(readableOrderCoffee -> {
                                        OrderCoffeeResponseDto orderCoffeeResponseDto =
                                                new OrderCoffeeResponseDto(readableOrderCoffee.getCoffeeId(),
                                                        readableOrderCoffee.getKorName(),
                                                        readableOrderCoffee.getEngName(),
                                                        readableOrderCoffee.getPrice(),
                                                        readableOrderCoffee.getQuantity());
                                        return orderCoffeeResponseDto;
                                    }).collect(Collectors.toList());
                    orderResponseDto.setOrderCoffees(orderCoffeeResponseDtos);

                    return orderResponseDto;
                }).collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Step 4: 최근 주문 순으로 정렬하기
    @GetMapping("/no-nplus1/v4")
    public ResponseEntity getOrdersNoNPlus1V4() {
        List<ReadableOrderCoffee> orders = orderService.findOrders2();

        Map<ReadableOrderGroupDto, List<ReadableOrderCoffee>> grouped =
                orders.stream().collect(
                        Collectors.groupingBy(readableOrderCoffee -> new ReadableOrderGroupDto(readableOrderCoffee)));

        List<OrderResponseDto> response = grouped.entrySet().stream()
                .map(e -> {
                    ReadableOrderGroupDto groupDto = e.getKey();
                    List<ReadableOrderCoffee> readableOrderCoffees = e.getValue();
                    OrderResponseDto orderResponseDto = new OrderResponseDto();
                    orderResponseDto.setOrderId(groupDto.getOrderId());
                    orderResponseDto.setMemberId(groupDto.getMemberId());
                    orderResponseDto.setOrderStatus(groupDto.getOrderStatus());
                    orderResponseDto.setCreatedAt(groupDto.getCreatedAt());

                    List<OrderCoffeeResponseDto> orderCoffeeResponseDtos =
                            readableOrderCoffees.stream()
                                    .map(readableOrderCoffee -> {
                                        OrderCoffeeResponseDto orderCoffeeResponseDto =
                                                new OrderCoffeeResponseDto(readableOrderCoffee.getCoffeeId(),
                                                        readableOrderCoffee.getKorName(),
                                                        readableOrderCoffee.getEngName(),
                                                        readableOrderCoffee.getPrice(),
                                                        readableOrderCoffee.getQuantity());
                                        return orderCoffeeResponseDto;
                                    }).collect(Collectors.toList());
                    orderResponseDto.setOrderCoffees(orderCoffeeResponseDtos);

                    return orderResponseDto;
                }).collect(Collectors.toList());

        // 최근 주문 순으로 정렬
        response.sort(Comparator.comparing(OrderResponseDto::getOrderId).reversed());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Step 5: OrderMapper를 이용해 코드 리팩토링
    @GetMapping("/no-nplus1/v5")
    public ResponseEntity getOrdersNoNPlus1V5() {
        List<ReadableOrderCoffee> orders = orderService.findOrders2();
        return new ResponseEntity<>(mapper.readableOrderCoffeeToOrderResponseDto(orders), HttpStatus.OK);
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity cancelOrder(@PathVariable("order-id") long orderId) {
        orderService.cancelOrder(orderId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/
}
