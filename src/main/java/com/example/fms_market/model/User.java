package com.example.fms_market.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"id", "username", "email", "password", "role", "phone", "age", "user_photo_path", "favoriteShowIds"})
public class User {
    private int id;
    private String email;
    private String password;
    private final String role;
    private String phone;
    private String age;
    private String user_photo_path;
    private List<Integer> favoriteShowIds;
    private String username;

    @JsonCreator
    public User(
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("role") String role,
            @JsonProperty("phone") String phone,
            @JsonProperty("age") String age,
            @JsonProperty("user_photo_path") String userPhotoPath) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.age = age;
        this.user_photo_path = userPhotoPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setFavoriteShowIds(List<Integer> favoriteShowIds) {
        this.favoriteShowIds = favoriteShowIds;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getAge() {
        return age;
    }

    public String getUser_photo_path() {
        return user_photo_path;
    }

    public void setUser_photo_path(String user_photo_path) {
        this.user_photo_path = user_photo_path;
    }

    public List<Integer> getFavoriteShowIds() {
        return favoriteShowIds;
    }

    public String getUsername() {
        return username;
    }
}