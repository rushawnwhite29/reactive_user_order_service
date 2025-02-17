package com.griddynamics.service;

import com.griddynamics.integration.ProductInfoServiceClient;
import com.griddynamics.integration.model.ProductInfoServiceModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

/**
 * Unit tests for the ProductService.
 * This class tests the ProductService's methods and their interactions with the ProductInfoServiceClient.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static final String PRODUCT_CODE = "product123";
    private static final String PRODUCT_NAME = "Product Name";
    private static final double PRODUCT_SCORE = 5.0;

    private static final ProductInfoServiceModel.Product PRODUCT = ProductInfoServiceModel.Product.builder()
            .productCode(PRODUCT_CODE)
            .productName(PRODUCT_NAME)
            .score(PRODUCT_SCORE)
            .build();

    @Mock
    private ProductInfoServiceClient productInfoServiceClient;

    @InjectMocks
    private ProductService productService;

    /**
     * Tests the successful retrieval of the most relevant product by product code.
     * Mocks the ProductInfoServiceClient to return a predefined product.
     */
    @Test
    public void testGetMostRelevantProductByScore_Success() {
        when(productInfoServiceClient.getProductNamesInfoByProductCode(PRODUCT_CODE)).thenReturn(Flux.just(PRODUCT));

        Mono<ProductInfoServiceModel.Product> result = productService.getMostRelevantProductByScore(PRODUCT_CODE);

        StepVerifier.create(result)
                .expectNextMatches(product -> product.getProductCode().equals(PRODUCT_CODE)
                        && product.getProductName().equals(PRODUCT_NAME)
                        && product.getScore() == PRODUCT_SCORE)
                .verifyComplete();
    }

    /**
     * Tests the scenario where no products are found for the given product code.
     * Mocks the ProductInfoServiceClient to return an empty Flux.
     */
    @Test
    public void testGetMostRelevantProductByScore_Empty() {
        when(productInfoServiceClient.getProductNamesInfoByProductCode(PRODUCT_CODE)).thenReturn(Flux.empty());

        Mono<ProductInfoServiceModel.Product> result = productService.getMostRelevantProductByScore(PRODUCT_CODE);

        StepVerifier.create(result)
                .expectNextMatches(product -> product.getProductCode().equals(PRODUCT_CODE)
                        && product.getProductName() == null
                        && product.getScore() == 0.0)
                .verifyComplete();
    }

    /**
     * Tests the scenario where the product info service returns a server error.
     * Mocks the ProductInfoServiceClient to return an error.
     */
    @Test
    public void testGetMostRelevantProductByScore_Error() {
        when(productInfoServiceClient.getProductNamesInfoByProductCode(PRODUCT_CODE))
                .thenReturn(Flux.error(new RuntimeException("Internal Server Error")));

        Mono<ProductInfoServiceModel.Product> result = productService.getMostRelevantProductByScore(PRODUCT_CODE);

        StepVerifier.create(result)
                .expectNextMatches(product -> product.getProductCode().equals(PRODUCT_CODE)
                        && product.getProductName() == null
                        && product.getScore() == 0.0)
                .verifyComplete();
    }

}