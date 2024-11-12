// ListPlanViewModel.java
package com.example.mobile.ui.food.Plan.List;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.mobile.database.repositories.PlanFoodRepository;
import com.example.mobile.dto.DayMealPlan;
import java.util.List;

public class ListPlanViewModel extends AndroidViewModel {

    private PlanFoodRepository planRepository;
    private LiveData<List<DayMealPlan>> allDayMealPlans;

    public ListPlanViewModel(Application application) {
        super(application);
        planRepository = new PlanFoodRepository(application);
        allDayMealPlans = planRepository.getAllDayMealPlans();
    }

    public LiveData<List<DayMealPlan>> getAllDayMealPlans() {
        return allDayMealPlans;
    }

    public void deleteAllData() {
        planRepository.deleteAllPlanFoodCrossRefs();
    }


}
