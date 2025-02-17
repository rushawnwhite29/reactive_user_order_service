package com.griddynamics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Domain model representing a user.
 * This class is mapped to the "users" collection in MongoDB.
 */
@Document(collection = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * The unique identifier for the user.
     */
    @MongoId
    private String id;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The phone number of the user.
     */
    private String phone;

}