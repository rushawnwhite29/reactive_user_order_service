package com.griddynamics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import com.griddynamics.integration.ProductInfoServiceClient;
import com.griddynamics.integration.model.ProductInfoServiceModel;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductService {

    private final ProductInfoServiceClient productInfoServiceClient;

    public Mono<ProductInfoServiceModel.Product> getMostRelevantProductByScore(String productCode) {
        return productInfoServiceClient.getProductNamesInfoByProductCode(productCode)
                .doOnComplete(() -> log.info("Fetched product names [{}]", productCode))
                .doOnError(e -> log.error("Error fetching product names [{}]. {}", productCode, e.getMessage()))
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
