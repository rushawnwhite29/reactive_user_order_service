package com.griddynamics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

import com.griddynamics.controller.model.ApiModel;
import com.griddynamics.integration.model.ProductInfoServiceModel;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderInfoService {

    private final UserInfoService userInfoService;
    private final OrderService orderService;
    private final ProductService productService;

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
                .doOnError(e -> log.error("Error fetching orders by user id [{}]. {}", userId, e.getMessage()))
                .onErrorResume(e -> Flux.empty());
    }

}
