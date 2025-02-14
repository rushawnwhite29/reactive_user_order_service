package com.griddynamics.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.griddynamics.controller.model.ApiModel;
import com.griddynamics.service.OrderInfoService;

@Log4j2
@RestController
@RequiredArgsConstructor
public class OrderInfoController {

    private final OrderInfoService orderService;

    @GetMapping("/orders/{phoneNumber}")
    public Flux<ApiModel.OrderInfo> getOrdersByPhoneNumber(@PathVariable String phoneNumber) {
        log.info("Received request to get orders by phone number: {}", phoneNumber);
        return orderService.getOrdersByPhoneNumber(phoneNumber);
    }

}
