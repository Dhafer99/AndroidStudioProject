package com.example.mobile.database.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mobile.DAO.PlanFoodCrossRefDao;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.relations.PlanFoodCrossRef;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlanFoodCrossRefRepository {

    private PlanFoodCrossRefDao planFoodCrossRefDao;
    private ExecutorService executorService;

    public PlanFoodCrossRefRepository(Application application) {
        PetCareDatabase db = DatabaseProvider.getDatabase(application);
        planFoodCrossRefDao = db.planFoodCrossRefDao();
        executorService = Executors.newSingleThreadExecutor();  // Executor for background tasks
    }

    // Insert PlanFoodCrossRef
    public void insert(PlanFoodCrossRef planFoodCrossRef) {
        executorService.execute(() -> planFoodCrossRefDao.insert(planFoodCrossRef));
    }

    // Delete PlanFoodCrossRef
    public void delete(PlanFoodCrossRef planFoodCrossRef) {
        executorService.execute(() -> planFoodCrossRefDao.delete(planFoodCrossRef));
    }

    // Delete all PlanFoodCrossRef entries
    public void deleteAll() {
        executorService.execute(() -> planFoodCrossRefDao.deleteAll());
    }

    public void deleteCrossRefsForPlan(long planId) {
        // Execute the deletion in a background thread
        DatabaseProvider.getDatabaseWriteExecutor().execute(() -> planFoodCrossRefDao.deleteCrossRefsForPlan(planId));
    }
}
