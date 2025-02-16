package com.griddynamics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import com.griddynamics.domain.model.User;
import com.griddynamics.repository.UserInfoRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    public Mono<User> findById(String id) {
        return userInfoRepository.findById(id)
                .doOnError(e -> log.error("Error fetching user by id [{}]. {}", id, e.getMessage()))
                .onErrorResume(e -> Mono.empty());
    }

}
