package com.ptithcm.moviebooking.schema;

public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String phone;
    private int age;

    public RegisterRequest(String email, String password, String name, String phone, int age) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.age = age;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

