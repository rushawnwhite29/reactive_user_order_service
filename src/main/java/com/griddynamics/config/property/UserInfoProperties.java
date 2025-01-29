package com.griddynamics.config.property;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.user-info")
public class UserInfoProperties {

    private int totInitUsers;

}
