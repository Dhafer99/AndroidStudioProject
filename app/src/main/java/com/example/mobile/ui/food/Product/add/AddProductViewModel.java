package com.example.mobile.ui.food.Product.add;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.repositories.FoodRepository;

import java.util.List;

import kotlinx.coroutines.Dispatchers;

public class AddProductViewModel extends AndroidViewModel {
    private final FoodRepository foodRepository;
    private final LiveData<List<FoodEntity>> allFoods;

    public AddProductViewModel(Application application) {
        super(application);
        foodRepository = new FoodRepository(application);
        allFoods = foodRepository.getAllFoods();
    }

    public LiveData<List<FoodEntity>> getAllFoods() {
        return allFoods;
    }

    public void insertFood(FoodEntity foodEntity) {
        foodRepository.insertFood(foodEntity);
    }

}


