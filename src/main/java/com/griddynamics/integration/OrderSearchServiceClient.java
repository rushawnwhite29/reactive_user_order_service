package com.griddynamics.integration;

import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.griddynamics.config.property.IntegrationProperties;
import com.griddynamics.domain.Order;

@Service
public class OrderSearchServiceClient {

    public static final String ORDERS_PATH = "/order/phone";
    public static final String PHONE_NUMBER_PARAM = "phoneNumber";

    private final WebClient orderServiceWebClient;

    private final String basePath;

    public OrderSearchServiceClient(WebClient orderServiceWebClient, IntegrationProperties integrationProperties) {
        this.orderServiceWebClient = orderServiceWebClient;
        this.basePath = integrationProperties.getOrderSearchService().getBasePath();
    }

    public Flux<Order> getOrdersByPhoneNumber(String phoneNumber) {
        return orderServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(basePath.concat(ORDERS_PATH))
                        .queryParam(PHONE_NUMBER_PARAM, phoneNumber)
                        .build())
                .retrieve()
                .bodyToFlux(Order.class)
                .log();
    }

}
