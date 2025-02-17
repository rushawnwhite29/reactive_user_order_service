package com.griddynamics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * Main application class for the Order Info service.
 * This class is responsible for bootstrapping the Spring Boot application.
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
@ConfigurationPropertiesScan
public class OrderInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderInfoApplication.class, args);
    }

}