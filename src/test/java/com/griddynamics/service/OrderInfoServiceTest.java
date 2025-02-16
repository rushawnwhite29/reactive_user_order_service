package com.griddynamics.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import com.griddynamics.controller.model.ApiModel;
import com.griddynamics.domain.model.User;
import com.griddynamics.integration.model.OrderSearchServiceModel;
import com.griddynamics.integration.model.ProductInfoServiceModel;

@ExtendWith(MockitoExtension.class)
public class OrderInfoServiceTest {

    @Mock
    private UserInfoService userInfoService;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderInfoService orderInfoService;

    private static final String USER_ID = "user123";
    private static final String PHONE = "1234567890";
    private static final String ORDER_NUMBER = "order123";
    private static final String PRODUCT_CODE = "product123";
    private static final String PRODUCT_NAME = "Product Name";
    private static final String USER_NAME = "John Doe";

    private static final User USER = User.builder()
            .id(USER_ID)
            .name(USER_NAME)
            .phone(PHONE)
            .build();

    private static final OrderSearchServiceModel.Order ORDER = OrderSearchServiceModel.Order.builder()
            .orderNumber(ORDER_NUMBER)
            .phoneNumber(PHONE)
            .productCode(PRODUCT_CODE)
            .build();

    private static final ProductInfoServiceModel.Product PRODUCT = ProductInfoServiceModel.Product.builder()
            .productCode(PRODUCT_CODE)
            .productName(PRODUCT_NAME)
            .build();

    @Test
    public void testGetOrdersByUserId_Success() {
        when(userInfoService.findById(USER_ID)).thenReturn(Mono.just(USER));
        when(orderService.getOrderByPhoneNumber(PHONE)).thenReturn(Flux.just(ORDER));
        when(productService.getMostRelevantProductByScore(PRODUCT_CODE)).thenReturn(Mono.just(PRODUCT));

        Flux<ApiModel.OrderInfo> result = orderInfoService.getOrdersByUserId(USER_ID);

        StepVerifier.create(result)
                .expectNextMatches(orderInfo -> orderInfo.getOrderNumber().equals(ORDER_NUMBER)
                        && orderInfo.getUserName().equals(USER_NAME)
                        && orderInfo.getPhoneNumber().equals(PHONE)
                        && orderInfo.getProductCode().equals(PRODUCT_CODE)
                        && orderInfo.getProductName().equals(PRODUCT_NAME))
                .verifyComplete();
    }

    @Test
    public void testGetOrdersByUserId_UserNotFound() {
        when(userInfoService.findById(USER_ID)).thenReturn(Mono.empty());

        Flux<ApiModel.OrderInfo> result = orderInfoService.getOrdersByUserId(USER_ID);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void testGetOrdersByUserId_NoOrders() {
        when(userInfoService.findById(USER_ID)).thenReturn(Mono.just(USER));
        when(orderService.getOrderByPhoneNumber(PHONE)).thenReturn(Flux.empty());

        Flux<ApiModel.OrderInfo> result = orderInfoService.getOrdersByUserId(USER_ID);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void testGetOrdersByUserId_ProductNotFound() {
        when(userInfoService.findById(USER_ID)).thenReturn(Mono.just(USER));
        when(orderService.getOrderByPhoneNumber(PHONE)).thenReturn(Flux.just(ORDER));
        when(productService.getMostRelevantProductByScore(PRODUCT_CODE)).thenReturn(Mono.just(ProductInfoServiceModel.Product.builder().productCode(PRODUCT_CODE).build()));

        Flux<ApiModel.OrderInfo> result = orderInfoService.getOrdersByUserId(USER_ID);

        StepVerifier.create(result)
                .expectNextMatches(orderInfo -> orderInfo.getOrderNumber().equals(ORDER_NUMBER)
                        && orderInfo.getUserName().equals(USER_NAME)
                        && orderInfo.getPhoneNumber().equals(PHONE)
                        && orderInfo.getProductCode().equals(PRODUCT_CODE)
                        && orderInfo.getProductName() == null)
                .verifyComplete();
    }

}