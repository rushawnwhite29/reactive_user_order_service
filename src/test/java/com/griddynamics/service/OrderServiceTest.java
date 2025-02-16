package com.griddynamics.service;

import com.griddynamics.integration.OrderSearchServiceClient;
import com.griddynamics.integration.model.OrderSearchServiceModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private static final String PHONE = "1234567890";
    private static final String ORDER_NUMBER = "order123";
    private static final String PRODUCT_CODE = "product123";

    private static final OrderSearchServiceModel.Order ORDER = OrderSearchServiceModel.Order.builder()
            .orderNumber(ORDER_NUMBER)
            .phoneNumber(PHONE)
            .productCode(PRODUCT_CODE)
            .build();

    @Mock
    private OrderSearchServiceClient orderSearchServiceClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testGetOrderByPhoneNumber_Success() {
        when(orderSearchServiceClient.getOrdersByPhoneNumber(PHONE)).thenReturn(Flux.just(ORDER));

        Flux<OrderSearchServiceModel.Order> result = orderService.getOrderByPhoneNumber(PHONE);

        StepVerifier.create(result)
                .expectNextMatches(order -> order.getOrderNumber().equals(ORDER_NUMBER)
                        && order.getPhoneNumber().equals(PHONE)
                        && order.getProductCode().equals(PRODUCT_CODE))
                .verifyComplete();
    }

    @Test
    public void testGetOrderByPhoneNumber_NotFound() {
        when(orderSearchServiceClient.getOrdersByPhoneNumber(PHONE)).thenReturn(Flux.empty());

        Flux<OrderSearchServiceModel.Order> result = orderService.getOrderByPhoneNumber(PHONE);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void testGetOrderByPhoneNumber_ServerError() {
        when(orderSearchServiceClient.getOrdersByPhoneNumber(PHONE))
                .thenReturn(Flux.error(new RuntimeException("Internal Server Error")));

        Flux<OrderSearchServiceModel.Order> result = orderService.getOrderByPhoneNumber(PHONE);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
}