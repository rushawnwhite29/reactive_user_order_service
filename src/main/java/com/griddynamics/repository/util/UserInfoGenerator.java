package com.griddynamics.repository.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Component;

import com.griddynamics.config.property.UserInfoProperties;
import com.griddynamics.domain.UserInfo;
import com.griddynamics.repository.UserInfoRepository;

@Component
@RequiredArgsConstructor
public class UserInfoGenerator {

    private final UserInfoProperties userInfoProperties;
    private final UserInfoRepository userInfoRepository;

    @PostConstruct
    public void generateUserInfo() {
        Flux.range(0, userInfoProperties.getTotInitUsers())
                .map(i -> new UserInfo(String.valueOf(i + 1)))
                .flatMap(userInfoRepository::save)
                .subscribe();
    }

}
