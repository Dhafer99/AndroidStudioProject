package com.example.mobile.ui.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mobile.R;
import com.example.mobile.databinding.FragmentServiceaddBinding;

public class serviceAddFragment extends Fragment {
    private FragmentServiceaddBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        serviceAddViewModel serviceAddViewModel = new ViewModelProvider(this).get(serviceAddViewModel.class);

        binding = FragmentServiceaddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button addService_btn = binding.addnewServiceBtn;
        Button showService_btn = binding.showServiceBtn;  // Make sure this ID matches your XML layout

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

        addService_btn.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_service_fares_to_nav_ajoutService);
        });

        showService_btn.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_service_fares_to_serviceaffichageFragment);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
