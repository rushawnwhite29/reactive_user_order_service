package com.griddynamics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

import com.griddynamics.controller.model.ApiModel;

/**
 * Service class for retrieving order information.
 * This service provides methods to fetch orders by user ID, including user and product details.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class OrderInfoService {

    private final UserInfoService userInfoService;
    private final OrderService orderService;
    private final ProductService productService;

    /**
     * Retrieves orders by user ID.
     * This method fetches user information, orders by phone number, and product details to construct order information.
     *
     * @param userId the user ID to search for orders
     * @return a Flux of ApiModel.OrderInfo objects containing the order details
     */
    public Flux<ApiModel.OrderInfo> getOrdersByUserId(String userId) {
        return userInfoService.findById(userId)
                .flux()
                .flatMap(user -> orderService.getOrderByPhoneNumber(user.getPhone())
                        .flatMap(order -> productService.getMostRelevantProductByScore(order.getProductCode())
                                .map(product -> ApiModel.OrderInfo.builder()
                                        .orderNumber(order.getOrderNumber())
                                        .userName(user.getName())
                                        .phoneNumber(user.getPhone())
                                        .productCode(product.getProductCode())
                                        .productName(product.getProductName())
                                        .productId(product.getProductId())
                                        .build()
                                )
                        )
                )
                .doOnError(e -> log.error("[ERROR] fetching orders by user id [{}]. {}", userId, e.getMessage()))
                .onErrorResume(e -> Flux.empty());
    }

}