package com.example.mobile.database.relations;

import androidx.room.Entity;


@Entity(primaryKeys = {"foodId", "planId"})

public class PlanFoodCrossRef {
    public long foodId;
    public long planId;

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }
}