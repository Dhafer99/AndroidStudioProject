package com.example.mobile.ui.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class serviceAddViewModel  extends ViewModel {
    private final MutableLiveData<String> text;

    public serviceAddViewModel() {
        text = new MutableLiveData<>();
        text.setValue("Service");
    }

    public LiveData<String> getText() {
        return text;
    }
}
