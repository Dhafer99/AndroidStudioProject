package com.example.mobile.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobile.database.relations.PlanFoodCrossRef;

@Dao
public interface PlanFoodCrossRefDao {

    @Insert
    void insert(PlanFoodCrossRef planFoodCrossRef);

    @Delete
    void delete(PlanFoodCrossRef planFoodCrossRef);

    // Delete all rows in the PlanFoodCrossRef table
    @Query("DELETE FROM PlanFoodCrossRef")
    void deleteAll();

    @Query("DELETE FROM planfoodcrossref WHERE planId = :planId")
    void deleteCrossRefsForPlan(long planId);
}
