package com.griddynamics.integration;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.griddynamics.config.property.IntegrationProperties;
import com.griddynamics.integration.model.ProductInfoServiceModel;

@Service
@Log4j2
public class ProductInfoServiceClient {

    public static final String PRODUCTS_PATH = "/product/names";
    public static final String PRODUCT_CODE_PARAM = "productCode";

    private final WebClient productServiceWebClient;

    private final String basePath;

    @Autowired
    public ProductInfoServiceClient(WebClient productServiceWebClient, IntegrationProperties integrationProperties) {
        this.productServiceWebClient = productServiceWebClient;
        this.basePath = integrationProperties.getOrderSearchService().getBasePath();
    }

    public Flux<ProductInfoServiceModel.Product> getProductNamesInfoByProductCode(String productCode) {
        return productServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(basePath.concat(PRODUCTS_PATH))
                        .queryParam(PRODUCT_CODE_PARAM, productCode)
                        .build())
                .retrieve()
                .bodyToFlux(ProductInfoServiceModel.Product.class)
                .doOnComplete(() -> log.info("Fetched product names info by product code: {}", productCode))
                .doOnError(e -> log.error("Error fetching product: {}", productCode, e))
                .onErrorReturn(new ProductInfoServiceModel.Product());
    }

}
