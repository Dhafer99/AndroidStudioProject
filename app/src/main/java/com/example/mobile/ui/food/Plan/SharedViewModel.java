package com.example.mobile.ui.food.Plan;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.PlanFoodEntity;
import com.example.mobile.database.relations.PlanFoodCrossRef;
import com.example.mobile.database.repositories.FoodRepository;
import com.example.mobile.database.repositories.PlanFoodCrossRefRepository;
import com.example.mobile.database.repositories.PlanFoodRepository;
import com.example.mobile.dto.DayMealPlan;

import java.util.List;
public class SharedViewModel extends AndroidViewModel {
    private PlanFoodRepository planRepository;
    private FoodRepository foodRepository;
    private LiveData<List<DayMealPlan>> allDayMealPlans;
    private LiveData<List<FoodEntity>> foods;
    private PlanFoodCrossRefRepository planFoodCrossRefRepository;

    public SharedViewModel(Application application) {
        super(application);

        // Initialize repositories
        planRepository = new PlanFoodRepository(application);
        foodRepository = new FoodRepository(application);
        planFoodCrossRefRepository = new PlanFoodCrossRefRepository(application);

        // Directly reference LiveData from repository
        allDayMealPlans = planRepository.getAllDayMealPlans();
        foods = foodRepository.getAllFoods();
    }

    public LiveData<List<FoodEntity>> getFoods() {
        return foods;
    }

    public LiveData<List<DayMealPlan>> getAllDayMealPlans() {
        return allDayMealPlans;
    }

    public void savePlanAndAssociateFoods(String jour, String type, List<FoodEntity> selectedFoods) {
        PlanFoodEntity planFoodEntity = new PlanFoodEntity(jour, type);
        long planId = planRepository.insert(planFoodEntity);

        for (FoodEntity food : selectedFoods) {
            PlanFoodCrossRef crossRef = new PlanFoodCrossRef();
            crossRef.setPlanId(planId);
            crossRef.setFoodId(food.getFoodId());
            planFoodCrossRefRepository.insert(crossRef);
        }
        //allDayMealPlans = planRepository.getAllDayMealPlans();
    }

    public void deleteAllData() {
        planRepository.deleteAllPlanFoodCrossRefs();
    }

    public void deletePlansByDay(String day) {
        planRepository.getPlansByDay(day).observeForever(new Observer<List<PlanFoodEntity>>() {
            @Override
            public void onChanged(List<PlanFoodEntity> plansForDay) {
                if (plansForDay != null && !plansForDay.isEmpty()) {
                    for (PlanFoodEntity plan : plansForDay) {
                        planFoodCrossRefRepository.deleteCrossRefsForPlan(plan.getPlanId());
                        planRepository.deletePlanFoodEntity(plan);
                    }
                    allDayMealPlans = planRepository.getAllDayMealPlans();
                }
            }
        });
    }



    public void deletePlan(String day) {
        planRepository.deletePlansByDay(day);
    }

}

