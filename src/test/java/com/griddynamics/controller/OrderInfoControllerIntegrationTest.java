package com.griddynamics.controller;

import com.griddynamics.config.TestConfig;
import com.griddynamics.controller.model.ApiModel;
import com.griddynamics.domain.model.User;
import com.griddynamics.repository.UserInfoRepository;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class OrderInfoControllerIntegrationTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private WireMockServer orderServiceWireMockServer;

    @Autowired
    private WireMockServer productServiceWireMockServer;

    private static final String USER_ID = "user123";
    private static final String ORDER_NUMBER = "order123";
    private static final String PRODUCT_ID = "prod123";
    private static final String PRODUCT_CODE = "product123";
    private static final String PRODUCT_NAME = "Product Name";
    private static final String USER_NAME = "John Doe";
    private static final String PHONE = "1234567890";
    private static final String PRODUCT_SCORE = "5.0";
    private static final String PRODUCT_RESPONSE_BODY = """
            [
                {
                    "productId":"%s",
                    "productCode":"%s",
                    "productName":"%s",
                    "score":%s
                }
            ]
    """.formatted(PRODUCT_ID, PRODUCT_CODE, PRODUCT_NAME, PRODUCT_SCORE);
    private static final String ORDER_RESPONSE_BODY = """
            [
                {
                    "orderNumber":"%s",
                    "phoneNumber":"%s",
                    "productCode":"%s"
                }
            ]
    """.formatted(ORDER_NUMBER, PHONE, PRODUCT_CODE);

    private static final ApiModel.OrderInfo ORDER_INFO = ApiModel.OrderInfo.builder()
            .orderNumber(ORDER_NUMBER)
            .userName(USER_NAME)
            .phoneNumber(PHONE)
            .productCode(PRODUCT_CODE)
            .productName(PRODUCT_NAME)
            .build();

    private static final String ORDER_INFO_PATH = "/orders/{userId}";

    private static final String ORDER_PATH = "/orderSearchService/order/phone";
    private static final String PHONE_PARAM = "phoneNumber";

    private static final String PRODUCTS_PATH = "/productInfoService/product/names";
    private static final String PRODUCT_CODE_PARAM = "productCode";

    @BeforeEach
    public void setUp() {
        orderServiceWireMockServer.resetAll();
        productServiceWireMockServer.resetAll();
    }

    @Test
    public void testGetOrdersByUserId_Success() {
        mockUserInfoRepository();
        orderServiceWireMockServer.stubFor(get(urlPathEqualTo(ORDER_PATH))
                                                   .withQueryParam(PHONE_PARAM, equalTo(PHONE))
                                                   .willReturn(aResponse()
                                                                       .withHeader("Content-Type", "application/json")
                                                                       .withBody(ORDER_RESPONSE_BODY)));

        productServiceWireMockServer.stubFor(get(urlPathEqualTo(PRODUCTS_PATH))
                                                     .withQueryParam(PRODUCT_CODE_PARAM, equalTo(PRODUCT_CODE))
                                                     .willReturn(aResponse()
                                                                         .withHeader("Content-Type", "application/json")
                                                                         .withBody(PRODUCT_RESPONSE_BODY)));

        webTestClient.get()
                .uri(ORDER_INFO_PATH, USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ApiModel.OrderInfo.class)
                .hasSize(1)
                .value(orderInfos -> {
                    assertThat(orderInfos).contains(ORDER_INFO);
                });
    }

    @Test
    public void testGetOrdersByUserId_UserNotFound() {
        when(userInfoRepository.findById(USER_ID)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri(ORDER_INFO_PATH, "nonexistentUser")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ApiModel.OrderInfo.class)
                .hasSize(0);
    }

    @Test
    public void testGetOrdersByUserId_NoOrdersFound() {
        mockUserInfoRepository();
        orderServiceWireMockServer.stubFor(get(urlPathEqualTo(ORDER_PATH))
                                                   .withQueryParam(PHONE_PARAM, equalTo(PHONE))
                                                   .willReturn(aResponse()
                                                                       .withHeader("Content-Type", "application/json")
                                                                       .withBody("[]")));

        productServiceWireMockServer.stubFor(get(urlPathEqualTo(PRODUCTS_PATH))
                                                     .withQueryParam(PRODUCT_CODE_PARAM, equalTo(PRODUCT_CODE))
                                                     .willReturn(aResponse()
                                                                         .withHeader("Content-Type", "application/json")
                                                                         .withBody(PRODUCT_RESPONSE_BODY)));

        webTestClient.get()
                .uri(ORDER_INFO_PATH, USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ApiModel.OrderInfo.class)
                .hasSize(0);
    }

    @Test
    public void testGetOrdersByUserId_NoProductsFound() {
        mockUserInfoRepository();
        orderServiceWireMockServer.stubFor(get(urlPathEqualTo(ORDER_PATH))
                                                   .withQueryParam(PHONE_PARAM, equalTo(PHONE))
                                                   .willReturn(aResponse()
                                                                       .withHeader("Content-Type", "application/json")
                                                                       .withBody(ORDER_RESPONSE_BODY)));

        productServiceWireMockServer.stubFor(get(urlPathEqualTo(PRODUCTS_PATH))
                                                     .withQueryParam(PRODUCT_CODE_PARAM, equalTo(PRODUCT_CODE))
                                                     .willReturn(aResponse()
                                                                         .withHeader("Content-Type", "application/json")
                                                                         .withBody("[]")));

        ApiModel.OrderInfo orderInfoWithoutProduct = ApiModel.OrderInfo.builder()
                .orderNumber(ORDER_NUMBER)
                .userName(USER_NAME)
                .phoneNumber(PHONE)
                .productCode(null)
                .productName(null)
                .build();

        webTestClient.get()
                .uri(ORDER_INFO_PATH, USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ApiModel.OrderInfo.class)
                .hasSize(1)
                .value(orderInfos -> {
                    assertThat(orderInfos).contains(orderInfoWithoutProduct);
                });
    }

    private void mockUserInfoRepository() {
        User user = User.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .phone(PHONE)
                .build();
        when(userInfoRepository.findById(USER_ID)).thenReturn(Mono.just(user));
    }

}