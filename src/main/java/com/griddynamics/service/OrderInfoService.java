package com.griddynamics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<ApiModel.OrderInfo> getOrdersByPhoneNumber(String phoneNumber) {
        return orderService.getOrderByPhoneNumber(phoneNumber)
                .flatMap(order -> userInfoService.findById(order.getPhoneNumber())
                        .flatMap(user -> productService.getMostRelevantProductByScore(order.getProductCode())
                                .map(product -> ApiModel.OrderInfo.builder()
                                        .orderNumber(order.getOrderNumber())
                                        .userName(user.getUserName())
                                        .phoneNumber(user.getPhoneNumber())
                                        .productCode(product.getProductCode())
                                        .productName(product.getProductName())
                                        .productId(product.getProductId())
                                        .build()
                                )
                        ));
    }

}
