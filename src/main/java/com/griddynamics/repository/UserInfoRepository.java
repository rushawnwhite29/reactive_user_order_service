package com.griddynamics.repository;

import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.griddynamics.domain.model.User;

public interface UserInfoRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findUserByPhone(String phone);

}
