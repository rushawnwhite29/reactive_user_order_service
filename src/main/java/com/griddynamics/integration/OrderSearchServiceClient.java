package com.griddynamics.integration;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.griddynamics.config.property.IntegrationProperties;
import com.griddynamics.integration.model.OrderSearchServiceModel;

@Service
@Log4j2
public class OrderSearchServiceClient {

    public static final String ORDERS_PATH = "/order/phone";
    public static final String PHONE_NUMBER_PARAM = "phoneNumber";

    private final WebClient orderServiceWebClient;

    private final String basePath;

    public OrderSearchServiceClient(WebClient orderServiceWebClient, IntegrationProperties integrationProperties) {
        this.orderServiceWebClient = orderServiceWebClient;
        this.basePath = integrationProperties.getOrderSearchService().getBasePath();
    }

    public Flux<OrderSearchServiceModel.Order> getOrdersByPhoneNumber(String phoneNumber) {
        return orderServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(basePath.concat(ORDERS_PATH))
                        .queryParam(PHONE_NUMBER_PARAM, phoneNumber)
                        .build())
                .retrieve()
                .bodyToFlux(OrderSearchServiceModel.Order.class)
                .doOnComplete(() -> log.info("Fetched orders by phone number: {}", phoneNumber))
                .doOnError(e -> log.error("Error fetching orders by phone number: {}", phoneNumber, e))
                .onErrorReturn(new OrderSearchServiceModel.Order());
    }

}
