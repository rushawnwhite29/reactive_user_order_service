package com.griddynamics.integration;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.griddynamics.config.property.IntegrationProperties;
import com.griddynamics.integration.model.OrderSearchServiceModel;

/**
 * Service client for interacting with the Order Search Service.
 * This client provides methods to retrieve order information by phone number.
 */
@Service
@Log4j2
public class OrderSearchServiceClient {

    public static final String ORDERS_PATH = "/order/phone";
    public static final String PHONE_NUMBER_PARAM = "phoneNumber";

    private final WebClient orderServiceWebClient;
    private final String basePath;

    /**
     * Constructs an OrderSearchServiceClient with the specified WebClient and integration properties.
     *
     * @param orderServiceWebClient the WebClient for making HTTP requests
     * @param integrationProperties the properties containing configuration for the integration
     */
    public OrderSearchServiceClient(WebClient orderServiceWebClient, IntegrationProperties integrationProperties) {
        this.orderServiceWebClient = orderServiceWebClient;
        this.basePath = integrationProperties.getOrderSearchService().getBasePath();
    }

    /**
     * Retrieves orders by phone number.
     * This method makes a GET request to the Order Search Service to fetch orders associated with the given phone number.
     *
     * @param phoneNumber the phone number to search for orders
     * @return a Flux of Order objects containing the order details
     */
    public Flux<OrderSearchServiceModel.Order> getOrdersByPhoneNumber(String phoneNumber) {
        log.info("Fetching orders for phone number: [{}]", phoneNumber);
        return orderServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(basePath.concat(ORDERS_PATH))
                        .queryParam(PHONE_NUMBER_PARAM, phoneNumber)
                        .build())
                .retrieve()
                .bodyToFlux(OrderSearchServiceModel.Order.class);
    }

}