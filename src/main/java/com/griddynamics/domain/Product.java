package com.griddynamics.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private final String productId;
    private final String productCode;
    private final String productName;
    private final double score;
}
