package com.griddynamics.config.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for external service integrations.
 * This class holds the configuration for the Product Info Service and Order Search Service.
 */
@Data
@ConfigurationProperties(prefix = "app.integration")
public class IntegrationProperties {

    private final ProductInfoService productInfoService;
    private final OrderSearchService orderSearchService;

    /**
     * Abstract base class for integration service properties.
     * Contains common properties such as URL and base path.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public abstract static class IntegrationService {
        private String url;
        private String basePath;
    }

    /**
     * Configuration properties for the Product Info Service.
     */
    public static class ProductInfoService extends IntegrationService {
        public ProductInfoService(String url, String basePath) {
            super(url, basePath);
        }
    }

    /**
     * Configuration properties for the Order Search Service.
     */
    public static class OrderSearchService extends IntegrationService {
        public OrderSearchService(String url, String basePath) {
            super(url, basePath);
        }
    }

}