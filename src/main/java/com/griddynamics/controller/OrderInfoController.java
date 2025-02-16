package com.griddynamics.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.griddynamics.controller.model.ApiModel;
import com.griddynamics.service.OrderInfoService;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderInfoController {

    private final OrderInfoService orderService;

    @GetMapping("/{userId}")
    public Flux<ApiModel.OrderInfo> getOrdersByPhone(@PathVariable String userId) {
        log.info("Received request to get orders by user id: [{}]", userId);
        return orderService.getOrdersByUserId(userId);
    }

}
