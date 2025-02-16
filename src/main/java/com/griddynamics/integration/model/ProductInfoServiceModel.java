package com.griddynamics.integration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API model interface for the Product Information Service.
 * This interface contains data transfer objects related to product information.
 */
public interface ProductInfoServiceModel {

    /**
     * Data transfer object representing a product.
     * This class includes details about the product such as product ID, product code, product name, and score.
     */
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