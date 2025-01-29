package com.griddynamics.config.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.integration")
public class IntegrationProperties {

    private final ProductInfoService productInfoService;
    private final OrderSearchService orderSearchService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public abstract static class IntegrationService {
        private String url;
        private String basePath;
    }

    public static
    class ProductInfoService extends IntegrationService {

        public ProductInfoService(String url, String basePath) {
            super(url, basePath);
        }
    }

    public static class OrderSearchService extends IntegrationService {

        public OrderSearchService(String url, String basePath) {
            super(url, basePath);
        }
    }
}
