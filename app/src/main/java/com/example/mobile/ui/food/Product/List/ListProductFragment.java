package com.example.mobile.ui.food.Product.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobile.databinding.FragmentListplanBinding;

public class ListProductFragment extends Fragment {

    private FragmentListplanBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListProductViewModel listProductViewModel = new ViewModelProvider(this).get(ListProductViewModel.class);

        binding = FragmentListplanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
