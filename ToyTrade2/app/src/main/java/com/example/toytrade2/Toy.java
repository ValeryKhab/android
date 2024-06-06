package com.example.toytrade2;

public class Toy {
    private final int userId;
    private final String name;
    private final String condition;
    private final String category;
    private final String ageCategory;
    private final String description;
    private final String exchangePreferences;
    private final int addressId;
    private final boolean isActive;

    public Toy(int userId, String name, String condition, String category, String ageCategory, String description, String exchangePreferences, int addressId, boolean isActive) {
        this.userId = userId;
        this.name = name;
        this.condition = condition;
        this.category = category;
        this.ageCategory = ageCategory;
        this.description = description;
        this.exchangePreferences = exchangePreferences;
        this.addressId = addressId;
        this.isActive = isActive;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getCondition() {
        return condition;
    }

    public String getCategory() {
        return category;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public String getDescription() {
        return description;
    }

    public String getExchangePreferences() {
        return exchangePreferences;
    }

    public int getAddressId() {
        return addressId;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
