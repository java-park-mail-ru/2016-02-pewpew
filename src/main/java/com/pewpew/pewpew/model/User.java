package com.pewpew.pewpew.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

@Entity(value = "users")
public class User {
    @Id
    private ObjectId id;
    private String password;
    @Indexed(unique = true)
    private String email;
    private String token;
    private Integer rating;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public ObjectId getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
