package com.griddynamics.integration;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.griddynamics.config.property.IntegrationProperties;
import com.griddynamics.integration.model.ProductInfoServiceModel;

/**
 * Service client for interacting with the Product Information Service.
 * This client provides methods to retrieve product information by product code.
 */
@Service
@Log4j2
public class ProductInfoServiceClient {

    public static final String PRODUCTS_PATH = "/product/names";
    public static final String PRODUCT_CODE_PARAM = "productCode";

    private final WebClient productServiceWebClient;
    private final String basePath;

    /**
     * Constructs a ProductInfoServiceClient with the specified WebClient and integration properties.
     *
     * @param productServiceWebClient the WebClient for making HTTP requests
     * @param integrationProperties the properties containing configuration for the integration
     */
    @Autowired
    public ProductInfoServiceClient(WebClient productServiceWebClient, IntegrationProperties integrationProperties) {
        this.productServiceWebClient = productServiceWebClient;
        this.basePath = integrationProperties.getProductInfoService().getBasePath();
    }

    /**
     * Retrieves product information by product code.
     * This method makes a GET request to the Product Information Service to fetch product details associated with the given product code.
     *
     * @param productCode the product code to search for product information
     * @return a Flux of Product objects containing the product details
     */
    public Flux<ProductInfoServiceModel.Product> getProductNamesInfoByProductCode(String productCode) {
        log.info("Fetching product information for product code: [{}]", productCode);
        return productServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(basePath.concat(PRODUCTS_PATH))
                        .queryParam(PRODUCT_CODE_PARAM, productCode)
                        .build())
                .retrieve()
                .bodyToFlux(ProductInfoServiceModel.Product.class);
    }

}