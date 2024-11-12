package com.example.mobile.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "planfood")
public class PlanFoodEntity {
    @PrimaryKey(autoGenerate = true)
    private long planId;
    private String jour;
    private String type;

    public PlanFoodEntity() {
    }

    public PlanFoodEntity(String jour, String type) {
        this.jour = jour;
        this.type = type;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}