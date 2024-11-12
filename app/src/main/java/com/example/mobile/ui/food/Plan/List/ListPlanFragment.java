package com.example.mobile.ui.food.Plan.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.Observer;

import com.example.mobile.R;
import com.example.mobile.databinding.FragmentListplanBinding;
import com.example.mobile.dto.DayMealPlan;
import com.example.mobile.ui.food.Plan.SharedViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListPlanFragment extends Fragment {

    private FragmentListplanBinding binding;
    private RecyclerView recyclerView;
    private DayMealPlanAdapter dayMealPlanAdapter;
    private SharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        binding = FragmentListplanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        dayMealPlanAdapter = new DayMealPlanAdapter(new ArrayList<>(),
                new DayMealPlanAdapter.OnItemDeleteListener() {
                    @Override
                    public void onItemDelete(DayMealPlan dayMealPlan) {
                        // Call ViewModel to delete the food item
                        sharedViewModel.deletePlansByDay(dayMealPlan.getJour());
                    }
                });
        recyclerView.setAdapter(dayMealPlanAdapter);


        sharedViewModel.getAllDayMealPlans().observe(getViewLifecycleOwner(), new Observer<List<DayMealPlan>>() {
            @Override
            public void onChanged(List<DayMealPlan> dayMealPlans) {
                dayMealPlanAdapter.updateData(dayMealPlans);
            }
        });


        FloatingActionButton btnAdd = binding.fabAdd;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the add page
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_listPlanFragment_to_addPlanFragment);
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
