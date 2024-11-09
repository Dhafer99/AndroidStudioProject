package com.example.mobile.ui.food.Product.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListProductViewModel extends ViewModel {

    private final MutableLiveData<String> text;

    public ListProductViewModel() {
        text = new MutableLiveData<>();
        text.setValue("Food");
    }

    public LiveData<String> getText() {
        return text;
    }
}

