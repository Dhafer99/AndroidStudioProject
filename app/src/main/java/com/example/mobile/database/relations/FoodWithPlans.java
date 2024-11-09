package com.example.mobile.database.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.PlanFoodEntity;

import java.util.List;

public class FoodWithPlans {
    @Embedded
    public FoodEntity food;
    @Relation(
            parentColumn = "foodId",
            entityColumn = "planId",
            associateBy = @Junction(PlanFoodCrossRef.class)
    )
    public List<PlanFoodEntity> plans;
}
