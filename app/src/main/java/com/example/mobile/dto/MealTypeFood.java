package com.example.mobile.dto;

import com.example.mobile.database.FoodEntity;

import java.util.List;

public class MealTypeFood {
    private String mealType;  // Meal type (e.g., breakfast, dinner)
    private List<FoodEntity> foodItems;  // List of foods for this meal type

    public MealTypeFood(String mealType, List<FoodEntity> foodItems) {
        this.mealType = mealType;
        this.foodItems = foodItems;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public List<FoodEntity> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodEntity> foodItems) {
        this.foodItems = foodItems;
    }
}
