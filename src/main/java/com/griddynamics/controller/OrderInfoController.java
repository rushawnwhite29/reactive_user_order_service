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

/**
 * REST controller for handling order information requests.
 * This controller provides endpoints to retrieve order information by user ID.
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderInfoController {

    private final OrderInfoService orderService;

    /**
     * Endpoint to get orders by user ID.
     * This method retrieves order information for a given user ID.
     *
     * @param userId the ID of the user whose orders are to be retrieved
     * @return a Flux of OrderInfo objects containing the order details
     */
    @GetMapping("/{userId}")
    public Flux<ApiModel.OrderInfo> getOrdersByPhone(@PathVariable String userId) {
        log.info("Received request to get orders by user id: [{}]", userId);
        return orderService.getOrdersByUserId(userId);
    }

}