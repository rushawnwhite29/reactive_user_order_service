package com.griddynamics.service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import com.griddynamics.domain.UserInfo;
import com.griddynamics.repository.UserInfoRepository;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    public Mono<UserInfo> findById(String id) {
        return userInfoRepository.findById(id);
    }

}
