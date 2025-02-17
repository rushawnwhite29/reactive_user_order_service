package com.griddynamics.integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API model interface for the Order Search Service.
 * This interface contains data transfer objects related to order search.
 */
public interface OrderSearchServiceModel {

    /**
     * Data transfer object representing an order.
     * This class includes details about the order such as phone number, order number, and product code.
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Order {
        private String phoneNumber;
        private String orderNumber;
        private String productCode;
    }

}