package com.griddynamics.integration.model;

import lombok.Data;

public interface OrderSearchServiceModel {

    @Data
    class Order {
        private String phoneNumber;
        private String orderNumber;
        private String productCode;
    }

}
