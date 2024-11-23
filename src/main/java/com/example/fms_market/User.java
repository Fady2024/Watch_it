package com.example.fms_market;

public class User {
    private final String email;
    private final String password;
    private final String role;
    private final String Phone;
    private final String age;
    private final String user_photo_path;



    public User(String email, String password, String role, String phone, String age, String userPhotoPath) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.Phone = phone;
        this.age = age;
        user_photo_path = userPhotoPath;
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
        return Phone;
    }
    public String getAge() {
        return age;
    }
    public String getUser_photo_path() {
        return user_photo_path;
    }
}
