package com.griddynamics.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.griddynamics.domain.UserInfo;

@Repository
public interface UserInfoRepository extends ReactiveCrudRepository<UserInfo, String> {
}
