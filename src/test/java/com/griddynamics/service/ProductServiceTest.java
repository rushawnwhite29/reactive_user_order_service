package com.griddynamics.service;

import com.griddynamics.integration.ProductInfoServiceClient;
import com.griddynamics.integration.model.ProductInfoServiceModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

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