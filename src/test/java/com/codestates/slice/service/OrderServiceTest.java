package com.codestates.slice.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.helper.StubData;
import com.codestates.order.entity.Order;
import com.codestates.order.repository.OrderRepository;
import com.codestates.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void cancelOrderTest() {
        // given
        long orderId = 1L;
        Order order = StubData.MockOrder.getSingleResponseBody(orderId);

        // when
        Executable executable = () -> orderService.cancelOrder(orderId);

        // then
        assertThrows(BusinessLogicException.class, executable);
    }
}
