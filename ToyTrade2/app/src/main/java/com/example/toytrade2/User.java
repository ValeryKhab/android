package com.example.toytrade2;

public class User {
    private final String name;
    private final String email;
    private final String phone;
    private final String password;
    private final byte[] photo;

    public User(String name, String email, String phone, String password, byte[] photo) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getPhoto() {
        return photo;
    }


}
