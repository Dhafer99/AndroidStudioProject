package com.example.mobile.ui.food.Plan.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobile.databinding.FragmentListplanBinding;

public class ListPlanFragment extends Fragment {

    private FragmentListplanBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListPlanViewModel listPlanViewModel = new ViewModelProvider(this).get(ListPlanViewModel.class);

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
