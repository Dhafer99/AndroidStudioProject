package com.example.mobile.ui.service.Ajout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobile.databinding.FragmentServiceaddBinding;

public class serviceajoutViewModel extends ViewModel {
    private final MutableLiveData<String> text;

    public serviceajoutViewModel() {
        text = new MutableLiveData<>();
        text.setValue("ajoutService");
    }

    public LiveData<String> getText() {
        return text;
    }
}
