package com.example.mobile.ui.food.Plan.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListPlanViewModel extends ViewModel {

    private final MutableLiveData<String> text;

    public ListPlanViewModel() {
        text = new MutableLiveData<>();
        text.setValue("Food");
    }

    public LiveData<String> getText() {
        return text;
    }
}

