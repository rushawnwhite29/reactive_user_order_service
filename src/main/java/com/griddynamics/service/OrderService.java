package com.griddynamics.service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

import com.griddynamics.integration.OrderSearchServiceClient;
import com.griddynamics.integration.model.OrderSearchServiceModel;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderSearchServiceClient orderSearchServiceClient;

    public Flux<OrderSearchServiceModel.Order> getOrderByPhoneNumber(String orderId) {
        return orderSearchServiceClient.getOrdersByPhoneNumber(orderId);
    }

}
