package com.example.mobile.ui.food.Product.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.repositories.FoodRepository;
import com.example.mobile.databinding.FragmentListplanBinding;
import com.example.mobile.databinding.FragmentListproductBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListProductFragment extends Fragment {

    private FragmentListproductBinding binding;
    private FoodAdapter adapter;
    private List<FoodEntity> fullFoodList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListProductViewModel listProductViewModel = new ViewModelProvider(this).get(ListProductViewModel.class);
        FoodRepository foodRepository = new FoodRepository(getActivity().getApplication());


        binding = FragmentListproductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize RecyclerView
        RecyclerView recyclerView = binding.recyclerViewProducts;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter and set it to the RecyclerView
        adapter = new FoodAdapter(new ArrayList<>(), new FoodAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDelete(FoodEntity food) {
                // Call ViewModel to delete the food item
                listProductViewModel.deleteFood(food);
            }
        }, foodRepository, getContext());
        recyclerView.setAdapter(adapter);

        // Observe data from ViewModel and update the adapter
        listProductViewModel.getAllFoods().observe(getViewLifecycleOwner(), new Observer<List<FoodEntity>>() {
            @Override
            public void onChanged(List<FoodEntity> foodEntities) {
                fullFoodList = foodEntities;
                adapter.setFoodList(foodEntities);
            }
        });

        // Setup search functionality
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Optionally handle query submission
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterFoodList(newText);
                return true;
            }
        });

        binding.spinnerFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterFoodList(binding.searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        FloatingActionButton btnAdd = binding.fabAdd;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the add page
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_listProductFragment_to_addProductFragment);
            }
        });

        return root;
    }

    private void filterFoodList(String query) {
        List<FoodEntity> filteredList = new ArrayList<>();

        String selectedType = binding.spinnerFilterType.getSelectedItem().toString();

        for (FoodEntity food : fullFoodList) {
            boolean matchesQuery = food.getNom().toLowerCase().contains(query.toLowerCase());

            boolean matchesType = selectedType.equals("All") ||
                    (selectedType.equals("Product") && food.getType().equalsIgnoreCase("product")) ||
                    (selectedType.equals("Recipe") && food.getType().equalsIgnoreCase("recipe"));

            if (matchesQuery && matchesType) {
                filteredList.add(food);
            }
        }

        adapter.setFoodList(filteredList);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
