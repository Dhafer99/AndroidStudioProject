package com.example.mobile.database.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.PlanFoodEntity;

import java.util.List;

public class PlanWithFoods {
    @Embedded
    public PlanFoodEntity plan;
    @Relation(
            parentColumn = "planId",
            entityColumn = "foodId",
            associateBy = @Junction(PlanFoodCrossRef.class)
    )
    public List<FoodEntity> foods;
}