package com.griddynamics.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.griddynamics.domain.model.User;

/**
 * Repository interface for accessing user information.
 * This interface extends ReactiveMongoRepository to provide reactive CRUD operations for User entities.
 */
public interface UserInfoRepository extends ReactiveMongoRepository<User, String> {

}