package com.example.mobile.ui.food;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoodViewModel extends ViewModel {

    private final MutableLiveData<String> text;

    public FoodViewModel() {
        text = new MutableLiveData<>();
        text.setValue("Food");
    }

    public LiveData<String> getText() {
        return text;
    }
}

