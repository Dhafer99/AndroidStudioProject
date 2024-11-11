package com.example.mobile.ui.service.affichage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.ui.service.Ajout.serviceajoutViewModel;

public class serviceaffichageFragment extends Fragment {

    private serviceajoutViewModel serviceViewModel;
    private serviceAdapter serviceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and set it to RecyclerView
        serviceAdapter = new serviceAdapter(getContext());
        recyclerView.setAdapter(serviceAdapter);

        // Set up ViewModel
        serviceViewModel = new ViewModelProvider(this).get(serviceajoutViewModel.class);

        // Observe the LiveData from the ViewModel and update the adapter
        serviceViewModel.getAllServices().observe(getViewLifecycleOwner(), serviceEntities -> {
            serviceAdapter.setServiceList(serviceEntities);
        });

        return view;
    }
}
