package com.griddynamics.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Test configuration class for setting up WireMock servers.
 * This class provides beans for WireMock servers used in integration tests.
 */
@TestConfiguration
public class TestConfig {

    /**
     * Creates and starts a WireMock server for the order service.
     *
     * @return the WireMock server instance for the order service
     */
    @Bean
    public WireMockServer orderServiceWireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8081));
        wireMockServer.start();
        return wireMockServer;
    }

    /**
     * Creates and starts a WireMock server for the product service.
     *
     * @return the WireMock server instance for the product service
     */
    @Bean
    public WireMockServer productServiceWireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8082));
        wireMockServer.start();
        return wireMockServer;
    }

}