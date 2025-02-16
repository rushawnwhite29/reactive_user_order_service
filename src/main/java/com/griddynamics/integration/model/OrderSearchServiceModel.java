package com.griddynamics.integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface OrderSearchServiceModel {

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
