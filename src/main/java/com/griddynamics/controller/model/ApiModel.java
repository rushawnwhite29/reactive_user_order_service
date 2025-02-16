package com.griddynamics.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * API model interface containing data transfer objects for the application.
 */
public interface ApiModel {

    /**
     * Data transfer object representing order information.
     * This class includes details about the order, user, and product.
     */
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class OrderInfo {
        private String orderNumber;
        private String userName;
        private String phoneNumber;
        private String productCode;
        private String productName;
        private String productId;
    }

}