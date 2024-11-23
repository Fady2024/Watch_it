package com.example.fms_market;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "email", "password", "role", "phone", "age", "user_photo_path"})
public class User {
    private int id;
    private String email;
    private final String password;
    private final String role;
    private String phone;
    private String age;
    private final String user_photo_path;

    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("role") String role,
                @JsonProperty("phone") String phone,
                @JsonProperty("age") String age,
                @JsonProperty("user_photo_path") String userPhotoPath) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.age = age;
        this.user_photo_path = userPhotoPath;
    }

    // Setter for id
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

    // Getter methods
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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
}
