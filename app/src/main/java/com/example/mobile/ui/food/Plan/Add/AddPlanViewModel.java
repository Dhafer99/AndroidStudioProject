package com.example.mobile.ui.food.Plan.Add;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.PlanFoodEntity;
import com.example.mobile.database.relations.PlanFoodCrossRef;
import com.example.mobile.database.repositories.FoodRepository;
import com.example.mobile.database.repositories.PlanFoodCrossRefRepository;
import com.example.mobile.database.repositories.PlanFoodRepository;
import com.example.mobile.dto.DayMealPlan;

import java.util.ArrayList;
import java.util.List;

public class AddPlanViewModel extends AndroidViewModel {

    private PlanFoodRepository planRepository;
    private FoodRepository foodRepository;
    private LiveData<List<FoodEntity>> foods;
    private MutableLiveData<List<DayMealPlan>> allDayMealPlans = new MutableLiveData<>();  // Internal MutableLiveData
    private PlanFoodCrossRefRepository planFoodCrossRefRepository;

    public AddPlanViewModel(Application application) {
        super(application);
        planRepository = new PlanFoodRepository(application);
        foodRepository = new FoodRepository(application);
        foods = foodRepository.getAllFoods();

        // Directly observe the LiveData from planRepository if it's a LiveData
        planRepository.getAllDayMealPlans().observeForever(newPlans -> {
            allDayMealPlans.setValue(newPlans); // Update the internal MutableLiveData
        });

        planFoodCrossRefRepository = new PlanFoodCrossRefRepository(application);
    }

    public LiveData<List<FoodEntity>> getFoods() {
        return foods;
    }

    // Return the internal MutableLiveData for observing and modification
    public MutableLiveData<List<DayMealPlan>> getPlans() {
        return allDayMealPlans;
    }

    public void savePlanAndAssociateFoods(String jour, String type, List<FoodEntity> selectedFoods) {
        // Save the Plan
        PlanFoodEntity planFoodEntity = new PlanFoodEntity(jour, type);
        long planId = planRepository.insert(planFoodEntity);  // This will return the planId of the saved plan

        // Now, associate the foods with the saved plan
        for (FoodEntity food : selectedFoods) {
            PlanFoodCrossRef crossRef = new PlanFoodCrossRef();
            crossRef.setPlanId(planId);
            crossRef.setFoodId(food.getFoodId());
            planFoodCrossRefRepository.insert(crossRef);  // Insert into PlanFoodCrossRef table
        }
    }

    public void addDayMealPlan(DayMealPlan newPlan) {
        List<DayMealPlan> currentPlans = allDayMealPlans.getValue();  // Get current value
        if (currentPlans == null) {
            currentPlans = new ArrayList<>();  // Initialize if null
        }
        currentPlans.add(newPlan);
        allDayMealPlans.setValue(currentPlans);  // Update MutableLiveData
    }
}
