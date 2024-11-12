package com.example.mobile.ui.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mobile.R;
import com.example.mobile.databinding.FragmentFoodBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FoodFragment extends Fragment {

    private FragmentFoodBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FoodViewModel foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        binding = FragmentFoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textFood;
        foodViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button button1 = binding.button1;
        Button button2 = binding.button2;

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        button1.setOnClickListener(v -> {
            navController.navigate(R.id.action_foodFragment_to_foodPlanFragment);
        });

        button2.setOnClickListener(v -> {
            navController.navigate(R.id.action_foodFragment_to_foodProductFragment);
        });

        FloatingActionButton btnAdd = binding.fabChat;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the add page
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_foodFragment_to_chatFragment);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
