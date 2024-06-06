package com.example.toytrade2;

public class Address {
    private final int userId;
    private final String address;

    public Address(int userId, String address) {
        this.userId = userId;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public String getAddress() {
        return address;
    }
}
