package com.griddynamics.repository.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

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
        if (userInfoProperties.getTotInitUsers() <= 0) {
            return;
        }

        userInfoRepository.count()
                .filter(count -> count == 0)
                .flatMapMany(count -> Flux.range(0, userInfoProperties.getTotInitUsers())
                        .map(i -> UserInfo.builder()
                                .phoneNumber(String.valueOf(i + 1))
                                .userName(String.format("user_%d", i + 1))
                                .build())
                        .flatMap(userInfoRepository::save))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

}
