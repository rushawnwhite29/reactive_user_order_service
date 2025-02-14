package com.griddynamics.integration.model;

import lombok.Data;

public interface ProductInfoServiceModel {

    @Data
    class Product {
        private String productId;
        private String productCode;
        private String productName;
        private Double score;
    }

}
