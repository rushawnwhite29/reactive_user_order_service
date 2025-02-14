package com.griddynamics.controller.model;

import lombok.Builder;
import lombok.Data;

public interface ApiModel {

    @Data
    @Builder
    class OrderInfo {
        private String orderNumber;
        private String userName;
        private String phoneNumber;
        private String productCode;
        private String productName;
        private String productId;
    }
}
