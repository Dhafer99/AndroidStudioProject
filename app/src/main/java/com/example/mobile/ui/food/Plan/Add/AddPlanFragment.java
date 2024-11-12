package com.example.mobile.ui.food.Plan.Add;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.mobile.R;
import com.example.mobile.database.FoodEntity;
import com.example.mobile.databinding.FragmentAddplanBinding;
import com.example.mobile.dto.DayMealPlan;
import com.example.mobile.dto.MealTypeFood;
import com.example.mobile.ui.food.Plan.SharedViewModel;
import com.example.mobile.ui.food.Product.add.AddProductViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddPlanFragment extends Fragment {

    private FragmentAddplanBinding binding;
    private AddPlanViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedViewModel = new ViewModelProvider(this).get(AddPlanViewModel.class);

        binding = FragmentAddplanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AutoCompleteTextView dropdownDay = root.findViewById(R.id.dropdown_day);
        String[] optionsDay = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapterDay = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, optionsDay);
        dropdownDay.setAdapter(adapterDay);

        AutoCompleteTextView dropdownType = root.findViewById(R.id.dropdown_type);
        String[] optionsType = {"Breakfast", "Lunch", "Dinner", "Snack", "Supplement"};
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, optionsType);
        dropdownType.setAdapter(adapterType);

        MultiAutoCompleteTextView dropdownMeal = root.findViewById(R.id.dropdown_meal);
        dropdownMeal.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        sharedViewModel.getFoods().observe(getViewLifecycleOwner(), meals -> {
            if (meals != null) {
                List<String> mealNames = new ArrayList<>();
                for (FoodEntity meal : meals) {
                    mealNames.add(meal.getNom());
                }
                ArrayAdapter<String> mealAdapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line, mealNames);
                dropdownMeal.setAdapter(mealAdapter);
            }
        });

        dropdownMeal.setOnClickListener(v -> dropdownMeal.showDropDown());
        dropdownDay.setOnClickListener(v -> dropdownDay.showDropDown());
        dropdownType.setOnClickListener(v -> dropdownType.showDropDown());

        Button saveButton = root.findViewById(R.id.addfood_btn);
        saveButton.setOnClickListener(v -> {
            String selectedDay = dropdownDay.getText().toString();
            String selectedType = dropdownType.getText().toString();
            String selectedMeals = dropdownMeal.getText().toString();

            if (TextUtils.isEmpty(selectedDay)) {
                dropdownDay.setError("Please select a day");
                return;
            }

            if (TextUtils.isEmpty(selectedType)) {
                dropdownType.setError("Please select a type of meal");
                return;
            }

            if (TextUtils.isEmpty(selectedMeals)) {
                dropdownMeal.setError("Please select at least one meal");
                return;
            }

            List<String> selectedMealNames = new ArrayList<>();
            if (!selectedMeals.isEmpty()) {
                String[] mealArray = selectedMeals.split(",");
                for (String meal : mealArray) {
                    selectedMealNames.add(meal.trim());
                }
            }

            Set<FoodEntity> selectedFoodsSet = new HashSet<>();
            List<FoodEntity> foodEntities = sharedViewModel.getFoods().getValue();
            if (foodEntities != null) {
                for (FoodEntity food : foodEntities) {
                    if (selectedMealNames.contains(food.getNom())) {
                        selectedFoodsSet.add(food);
                    }
                }
            }

            List<FoodEntity> selectedFoods = new ArrayList<>(selectedFoodsSet);

            sharedViewModel.savePlanAndAssociateFoods(selectedDay, selectedType, selectedFoods);

            // Optionally, create the DayMealPlan object
            MealTypeFood mealTypeFood = new MealTypeFood(selectedType, selectedFoods);
            List<MealTypeFood> mealTypes = new ArrayList<>();
            mealTypes.add(mealTypeFood);
            DayMealPlan newPlan = new DayMealPlan(selectedDay, mealTypes);

            // Clear the dropdown fields
            dropdownDay.setText("");
            dropdownType.setText("");
            dropdownMeal.setText("");

            Toast.makeText(requireContext(), "Plan saved successfully", Toast.LENGTH_LONG).show();

            // Navigate to ListPlanFragment and clear the back stack
            NavController navController = Navigation.findNavController(v);
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_host_fragment_content_main, true) // This pops up to the nav host
                    .build();
            navController.navigate(R.id.action_addPlanFragment_to_listPlanFragment, null, navOptions);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}