package com.griddynamics.controller;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.griddynamics.domain.UserInfo;
import com.griddynamics.service.UserInfoService;

@RestController
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping("/users/{id}")
    public Mono<UserInfo> getUserById(@PathVariable String id) {
        return userInfoService.findById(id);
    }

}
