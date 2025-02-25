package com.griddynamics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import com.griddynamics.integration.ProductInfoServiceClient;
import com.griddynamics.integration.model.ProductInfoServiceModel;

/**
 * Service class for retrieving product information.
 * This service provides methods to fetch the most relevant product by product code.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class ProductService {

    private final ProductInfoServiceClient productInfoServiceClient;

    /**
     * Retrieves the most relevant product by product code.
     * This method makes a call to the ProductInfoServiceClient to fetch product details associated with the given product code.
     *
     * @param productCode the product code to search for product information
     * @return a Mono of Product object containing the most relevant product details
     */
    public Mono<ProductInfoServiceModel.Product> getMostRelevantProductByScore(String productCode) {
        return productInfoServiceClient.getProductNamesInfoByProductCode(productCode)
                .doOnComplete(() -> log.info("FOUND product names [{}]", productCode))
                .doOnError(e -> log.error("[ERROR] fetching product names [{}]. {}", productCode, e.getMessage()))
                .onErrorReturn(
                        ProductInfoServiceModel.Product.builder()
                                .productCode(productCode)
                                .build()
                )
                .defaultIfEmpty(
                        ProductInfoServiceModel.Product.builder()
                                .productCode(productCode)
                                .build()
                )
                .reduce((product1, product2) -> product1.getScore() >= product2.getScore() ? product1 : product2);
    }

}