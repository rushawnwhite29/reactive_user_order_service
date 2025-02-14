package com.griddynamics.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.griddynamics.config.property.IntegrationProperties;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final IntegrationProperties integrationProperties;

    @Bean
    public WebClient orderServiceWebClient() {
        return WebClient.builder()
                .baseUrl(integrationProperties.getOrderSearchService().getUrl())
                .build();
    }

    @Bean
    public WebClient productServiceWebClient() {
        return WebClient.builder()
                .baseUrl(integrationProperties.getProductInfoService().getUrl())
                .build();
    }

}
