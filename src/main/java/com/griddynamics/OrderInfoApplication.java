package com.griddynamics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
@ConfigurationPropertiesScan
public class OrderInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderInfoApplication.class, args);
    }

}
