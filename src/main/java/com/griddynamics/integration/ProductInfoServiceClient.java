package com.griddynamics.integration;

import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.griddynamics.config.property.IntegrationProperties;
import com.griddynamics.domain.Product;

@Service
public class ProductInfoServiceClient {

    public static final String PRODUCTS_PATH = "/order/phone";
    public static final String PHONE_NUMBER_PARAM = "phoneNumber";

    private final WebClient productServiceWebClient;

    private final String basePath;

    public ProductInfoServiceClient(WebClient productServiceWebClient, IntegrationProperties integrationProperties) {
        this.productServiceWebClient = productServiceWebClient;
        this.basePath = integrationProperties.getOrderSearchService().getBasePath();
    }

    public Flux<Product> getProductNamesByProductCode(String productCode) {
        return productServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(basePath.concat(PRODUCTS_PATH))
                        .queryParam(PHONE_NUMBER_PARAM, productCode)
                        .build())
                .retrieve()
                .bodyToFlux(Product.class)
                .log();
    }

}
