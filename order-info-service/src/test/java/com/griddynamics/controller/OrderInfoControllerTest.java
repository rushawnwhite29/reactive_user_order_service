package com.griddynamics.controller;

import com.griddynamics.controller.model.ApiModel;
import com.griddynamics.service.OrderInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

/**
 * Unit tests for the OrderInfoController.
 * This class tests the OrderInfoController's endpoints and their interactions with the OrderInfoService.
 */
@ExtendWith(MockitoExtension.class)
public class OrderInfoControllerTest {

    @Mock
    private OrderInfoService orderInfoService;

    @InjectMocks
    private OrderInfoController orderInfoController;

    private WebTestClient webTestClient;

    private static final String USER_ID = "user123";
    private static final String ORDER_NUMBER = "order123";
    private static final String PRODUCT_CODE = "product123";
    private static final String PRODUCT_NAME = "Product Name";
    private static final String USER_NAME = "John Doe";
    private static final String PHONE = "1234567890";

    private static final ApiModel.OrderInfo ORDER_INFO = ApiModel.OrderInfo.builder()
            .orderNumber(ORDER_NUMBER)
            .userName(USER_NAME)
            .phoneNumber(PHONE)
            .productCode(PRODUCT_CODE)
            .productName(PRODUCT_NAME)
            .build();

    private static final String ORDER_INFO_PATH = "/orders/{userId}";

    /**
     * Sets up the WebTestClient before each test.
     */
    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToController(orderInfoController).build();
    }

    /**
     * Tests the successful retrieval of orders by user ID.
     * Mocks the OrderInfoService to return a predefined order.
     */
    @Test
    public void testGetOrdersByUserId_Success() {
        when(orderInfoService.getOrdersByUserId(USER_ID)).thenReturn(Flux.just(ORDER_INFO));

        webTestClient.get()
                .uri(ORDER_INFO_PATH, USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ApiModel.OrderInfo.class)
                .hasSize(1)
                .contains(ORDER_INFO);
    }

    /**
     * Tests the scenario where the user is not found.
     * Mocks the OrderInfoService to return an empty Flux.
     */
    @Test
    public void testGetOrdersByUserId_UserNotFound() {
        when(orderInfoService.getOrdersByUserId(USER_ID)).thenReturn(Flux.empty());

        webTestClient.get()
                .uri(ORDER_INFO_PATH, USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ApiModel.OrderInfo.class)
                .hasSize(0);
    }

    /**
     * Tests the scenario where no orders are found for the user.
     * Mocks the OrderInfoService to return an empty Flux.
     */
    @Test
    public void testGetOrdersByUserId_NoOrdersFound() {
        when(orderInfoService.getOrdersByUserId(USER_ID)).thenReturn(Flux.empty());

        webTestClient.get()
                .uri(ORDER_INFO_PATH, USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ApiModel.OrderInfo.class)
                .hasSize(0);
    }

    /**
     * Tests the scenario where no products are found for the orders.
     * Mocks the OrderInfoService to return an order without product details.
     */
    @Test
    public void testGetOrdersByUserId_NoProductsFound() {
        ApiModel.OrderInfo orderInfoWithoutProduct = ApiModel.OrderInfo.builder()
                .orderNumber(ORDER_NUMBER)
                .userName(USER_NAME)
                .phoneNumber(PHONE)
                .productCode(null)
                .productName(null)
                .build();

        when(orderInfoService.getOrdersByUserId(USER_ID)).thenReturn(Flux.just(orderInfoWithoutProduct));

        webTestClient.get()
                .uri(ORDER_INFO_PATH, USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ApiModel.OrderInfo.class)
                .hasSize(1)
                .contains(orderInfoWithoutProduct);
    }

}