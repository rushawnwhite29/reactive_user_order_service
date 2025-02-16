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

/**
 * Unit tests for the OrderSearchServiceClient.
 * This class tests the OrderSearchServiceClient's methods and their interactions with the external order search service.
 */
@SuppressWarnings("ALL")
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

    /**
     * Sets up the WireMock server and the OrderSearchServiceClient before each test.
     */
    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor(HOST, wireMockServer.port());

        WebClient webClient = WebClient.builder()
                .baseUrl("http://%s:%s".formatted(HOST, wireMockServer.port()))
                .build();

        IntegrationProperties integrationProperties = mock(IntegrationProperties.class);
        IntegrationProperties.OrderSearchService orderSearchService = mock(IntegrationProperties.OrderSearchService.class);
        when(integrationProperties.getOrderSearchService()).thenReturn(orderSearchService);
        when(orderSearchService.getBasePath()).thenReturn("");

        orderSearchServiceClient = new OrderSearchServiceClient(webClient, integrationProperties);
    }

    /**
     * Stops the WireMock server after each test.
     */
    @AfterEach
    void teardown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    /**
     * Tests the successful retrieval of orders by phone number.
     * Mocks the order search service to return a predefined order.
     */
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

    /**
     * Tests the scenario where no orders are found for the given phone number.
     * Mocks the order search service to return a 404 status.
     */
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

    /**
     * Tests the scenario where the order search service returns a server error.
     * Mocks the order search service to return a 500 status.
     */
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