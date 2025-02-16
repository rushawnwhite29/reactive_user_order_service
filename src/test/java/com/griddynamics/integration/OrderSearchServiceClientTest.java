package com.griddynamics.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.griddynamics.config.property.IntegrationProperties;
import com.griddynamics.integration.model.OrderSearchServiceModel;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(WireMockExtension.class)
public class OrderSearchServiceClientTest {

    private static final String HOST = "localhost";
    private static final String PHONE = "1234567890";
    private static final String ORDER_NUMBER = "order123";
    private static final String PRODUCT_CODE = "product123";
    private static final String ORDER_RESPONSE_BODY = """
            [
                {
                    "orderNumber":"%s",
                    "phoneNumber":"%s",
                    "productCode":"%s"
                }
            ]
    """.formatted(ORDER_NUMBER, PHONE, PRODUCT_CODE);

    private static final OrderSearchServiceModel.Order ORDER = OrderSearchServiceModel.Order.builder()
            .orderNumber(ORDER_NUMBER)
            .phoneNumber(PHONE)
            .productCode(PRODUCT_CODE)
            .build();

    private static final String ORDER_PATH = "/order/phone";
    private static final String PHONE_PARAM = "phoneNumber";

    private WireMockServer wireMockServer;
    private OrderSearchServiceClient orderSearchServiceClient;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor(HOST, wireMockServer.port());

        WebClient webClient = WebClient.builder()
                .baseUrl("http://" + HOST + ":" + wireMockServer.port())
                .build();

        IntegrationProperties integrationProperties = mock(IntegrationProperties.class);
        IntegrationProperties.OrderSearchService orderSearchService = mock(IntegrationProperties.OrderSearchService.class);
        when(integrationProperties.getOrderSearchService()).thenReturn(orderSearchService);
        when(orderSearchService.getBasePath()).thenReturn("");

        orderSearchServiceClient = new OrderSearchServiceClient(webClient, integrationProperties);
    }

    @AfterEach
    void teardown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    public void testGetOrdersByPhoneNumber_Success() {
        wireMockServer.stubFor(get(urlPathEqualTo(ORDER_PATH))
                                       .withQueryParam(PHONE_PARAM, equalTo(PHONE))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody(ORDER_RESPONSE_BODY)));

        Flux<OrderSearchServiceModel.Order> result = orderSearchServiceClient.getOrdersByPhoneNumber(PHONE);

        StepVerifier.create(result)
                .expectNextMatches(order -> order.equals(ORDER))
                .verifyComplete();
    }

    @Test
    public void testGetOrdersByPhoneNumber_NotFound() {
        wireMockServer.stubFor(get(urlPathEqualTo(ORDER_PATH))
                                       .withQueryParam(PHONE_PARAM, equalTo(PHONE))
                                       .willReturn(aResponse()
                                                           .withStatus(404)));

        Flux<OrderSearchServiceModel.Order> result = orderSearchServiceClient.getOrdersByPhoneNumber(PHONE);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof WebClientResponseException.NotFound)
                .verify();
    }

    @Test
    public void testGetOrdersByPhoneNumber_ServerError() {
        wireMockServer.stubFor(get(urlPathEqualTo(ORDER_PATH))
                                       .withQueryParam(PHONE_PARAM, equalTo(PHONE))
                                       .willReturn(aResponse()
                                                           .withStatus(500)));

        Flux<OrderSearchServiceModel.Order> result = orderSearchServiceClient.getOrdersByPhoneNumber(PHONE);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof WebClientResponseException.InternalServerError)
                .verify();
    }
}