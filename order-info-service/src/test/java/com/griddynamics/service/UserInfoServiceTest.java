package com.griddynamics.service;

import com.griddynamics.domain.model.User;
import com.griddynamics.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

/**
 * Unit tests for the UserInfoService.
 * This class tests the UserInfoService's methods and their interactions with the UserInfoRepository.
 */
@ExtendWith(MockitoExtension.class)
public class UserInfoServiceTest {

    private static final String USER_ID = "user123";
    private static final String USER_NAME = "John Doe";
    private static final String PHONE = "1234567890";

    private static final User USER = User.builder()
            .id(USER_ID)
            .name(USER_NAME)
            .phone(PHONE)
            .build();

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private UserInfoService userInfoService;

    /**
     * Tests the successful retrieval of user information by user ID.
     * Mocks the UserInfoRepository to return a predefined user.
     */
    @Test
    public void testFindById_Success() {
        when(userInfoRepository.findById(USER_ID)).thenReturn(Mono.just(USER));

        Mono<User> result = userInfoService.findById(USER_ID);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals(USER_ID)
                        && user.getName().equals(USER_NAME)
                        && user.getPhone().equals(PHONE))
                .verifyComplete();
    }

    /**
     * Tests the scenario where the user is not found.
     * Mocks the UserInfoRepository to return an empty Mono.
     */
    @Test
    public void testFindById_UserNotFound() {
        when(userInfoRepository.findById(USER_ID)).thenReturn(Mono.empty());

        Mono<User> result = userInfoService.findById(USER_ID);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    /**
     * Tests the scenario where an error occurs while retrieving user information.
     * Mocks the UserInfoRepository to return an error.
     */
    @Test
    public void testFindById_Error() {
        when(userInfoRepository.findById(USER_ID))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<User> result = userInfoService.findById(USER_ID);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

}