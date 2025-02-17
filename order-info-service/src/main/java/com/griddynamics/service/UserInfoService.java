package com.griddynamics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import com.griddynamics.domain.model.User;
import com.griddynamics.repository.UserInfoRepository;

/**
 * Service class for retrieving user information.
 * This service provides methods to fetch user details by user ID.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    /**
     * Retrieves user information by user ID.
     * This method makes a call to the UserInfoRepository to fetch user details associated with the given user ID.
     *
     * @param id the user ID to search for user information
     * @return a Mono of User object containing the user details
     */
    public Mono<User> findById(String id) {
        return userInfoRepository.findById(id)
                .doOnError(e -> log.error("Error fetching user by id [{}]. {}", id, e.getMessage()))
                .onErrorResume(e -> Mono.empty());
    }

}