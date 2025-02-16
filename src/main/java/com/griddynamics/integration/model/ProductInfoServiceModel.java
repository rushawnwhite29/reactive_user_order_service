package com.griddynamics.integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface ProductInfoServiceModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Product {
        private String productId;
        private String productCode;
        private String productName;
        private double score;
    }

}
