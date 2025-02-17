package com.griddynamics.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.griddynamics.config.property.IntegrationProperties;
import com.griddynamics.integration.model.ProductInfoServiceModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the ProductInfoServiceClient.
 * This class tests the ProductInfoServiceClient's methods and their interactions with the external product info service.
 */
@SuppressWarnings("ALL")
@ExtendWith(WireMockExtension.class)
public class ProductInfoServiceClientTest {

    private static final String HOST = "localhost";
    private static final String PRODUCT_ID = "prod123";
    private static final String PRODUCT_CODE = "product123";
    private static final String PRODUCT_NAME = "Product Name";
    private static final String PRODUCT_SCORE = "5.0";
    private static final String PRODUCT_RESPONSE_BODY = """
            [
                {
                    "productId":"%s",
                    "productCode":"%s",
                    "productName":"%s",
                    "score":%s
                }
            ]
    """.formatted(PRODUCT_ID, PRODUCT_CODE, PRODUCT_NAME, PRODUCT_SCORE);

    private static final ProductInfoServiceModel.Product PRODUCT = ProductInfoServiceModel.Product.builder()
            .productId(PRODUCT_ID)
            .productCode(PRODUCT_CODE)
            .productName(PRODUCT_NAME)
            .score(Double.parseDouble(PRODUCT_SCORE))
            .build();

    private static final String PRODUCTS_PATH = "/product/names";
    private static final String PRODUCT_CODE_PARAM = "productCode";

    private WireMockServer wireMockServer;
    private ProductInfoServiceClient productInfoServiceClient;

    /**
     * Sets up the WireMock server and the ProductInfoServiceClient before each test.
     */
    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor(HOST, wireMockServer.port());

        WebClient webClient = WebClient.builder()
                .baseUrl("http://%s:%s".formatted(HOST, wireMockServer.port()))
                .build();

        IntegrationProperties integrationProperties = mock(IntegrationProperties.class);
        IntegrationProperties.ProductInfoService productInfoService = mock(IntegrationProperties.ProductInfoService.class);
        when(integrationProperties.getProductInfoService()).thenReturn(productInfoService);
        when(productInfoService.getBasePath()).thenReturn("");

        productInfoServiceClient = new ProductInfoServiceClient(webClient, integrationProperties);
    }

    /**
     * Stops the WireMock server after each test.
     */
    @AfterEach
    void teardown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    /**
     * Tests the successful retrieval of product names info by product code.
     * Mocks the product info service to return a predefined product.
     */
    @Test
    public void testGetProductNamesInfoByProductCode_Success() {
        wireMockServer.stubFor(get(urlPathEqualTo(PRODUCTS_PATH))
                                       .withQueryParam(PRODUCT_CODE_PARAM, equalTo(PRODUCT_CODE))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody(PRODUCT_RESPONSE_BODY)));

        Flux<ProductInfoServiceModel.Product> result = productInfoServiceClient.getProductNamesInfoByProductCode(PRODUCT_CODE);

        StepVerifier.create(result)
                .expectNextMatches(product -> product.equals(PRODUCT))
                .verifyComplete();
    }

    /**
     * Tests the scenario where no products are found for the given product code.
     * Mocks the product info service to return a 404 status.
     */
    @Test
    public void testGetProductNamesInfoByProductCode_NotFound() {
        wireMockServer.stubFor(get(urlPathEqualTo(PRODUCTS_PATH))
                                       .withQueryParam(PRODUCT_CODE_PARAM, equalTo(PRODUCT_CODE))
                                       .willReturn(aResponse()
                                                           .withStatus(404)));

        Flux<ProductInfoServiceModel.Product> result = productInfoServiceClient.getProductNamesInfoByProductCode(PRODUCT_CODE);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof WebClientResponseException.NotFound)
                .verify();
    }

    /**
     * Tests the scenario where the product info service returns a server error.
     * Mocks the product info service to return a 500 status.
     */
    @Test
    public void testGetProductNamesInfoByProductCode_ServerError() {
        wireMockServer.stubFor(get(urlPathEqualTo(PRODUCTS_PATH))
                                       .withQueryParam(PRODUCT_CODE_PARAM, equalTo(PRODUCT_CODE))
                                       .willReturn(aResponse()
                                                           .withStatus(500)));

        Flux<ProductInfoServiceModel.Product> result = productInfoServiceClient.getProductNamesInfoByProductCode(PRODUCT_CODE);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof WebClientResponseException.InternalServerError)
                .verify();
    }
}