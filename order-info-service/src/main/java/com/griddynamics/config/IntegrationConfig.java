package com.griddynamics.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.griddynamics.config.property.IntegrationProperties;

/**
 * Configuration class for setting up WebClient beans for external service integrations.
 */
@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final IntegrationProperties integrationProperties;

    /**
     * Creates a WebClient bean for the Order Search Service.
     *
     * @return a configured WebClient instance for the Order Search Service
     */
    @Bean
    public WebClient orderServiceWebClient() {
        // Configure the WebClient with the base URL for the Order Search Service
        return WebClient.builder()
                .baseUrl(integrationProperties.getOrderSearchService().getUrl())
                .build();
    }

    /**
     * Creates a WebClient bean for the Product Info Service.
     *
     * @return a configured WebClient instance for the Product Info Service
     */
    @Bean
    public WebClient productServiceWebClient() {
        // Configure the WebClient with the base URL for the Product Info Service
        return WebClient.builder()
                .baseUrl(integrationProperties.getProductInfoService().getUrl())
                .build();
    }

}