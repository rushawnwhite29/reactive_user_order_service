package com.griddynamics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

import com.griddynamics.integration.OrderSearchServiceClient;
import com.griddynamics.integration.model.OrderSearchServiceModel;

/**
 * Service class for retrieving order information.
 * This service provides methods to fetch orders by phone number.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class OrderService {

    private final OrderSearchServiceClient orderSearchServiceClient;

    /**
     * Retrieves orders by phone number.
     * This method makes a call to the OrderSearchServiceClient to fetch orders associated with the given phone number.
     *
     * @param phone the phone number to search for orders
     * @return a Flux of Order objects containing the order details
     */
    public Flux<OrderSearchServiceModel.Order> getOrderByPhoneNumber(String phone) {
        return orderSearchServiceClient.getOrdersByPhoneNumber(phone)
                .doOnComplete(() -> log.info("Fetched orders by phone number [{}]", phone))
                .doOnError(e -> log.error("Error fetching orders by phone number [{}]. {}", phone, e))
                .onErrorResume(e -> Flux.empty());
    }

}