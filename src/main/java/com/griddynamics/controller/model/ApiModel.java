package com.griddynamics.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

public interface ApiModel {

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
