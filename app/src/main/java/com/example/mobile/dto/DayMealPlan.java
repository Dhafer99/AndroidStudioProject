package com.example.mobile.dto;

import java.util.List;
import java.util.Objects;

public class DayMealPlan {
    private String jour;  // The day (e.g., Monday)
    private List<MealTypeFood> mealTypes;  // List of meal types (breakfast, dinner, etc.)

    public DayMealPlan(String jour, List<MealTypeFood> mealTypes) {
        this.jour = jour;
        this.mealTypes = mealTypes;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public List<MealTypeFood> getMealTypes() {
        return mealTypes;
    }

    public void setMealTypes(List<MealTypeFood> mealTypes) {
        this.mealTypes = mealTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Vérifie si les deux objets sont identiques
        if (o == null || getClass() != o.getClass()) return false;  // Vérifie si l'objet est du bon type

        DayMealPlan that = (DayMealPlan) o;
        return Objects.equals(jour, that.jour) &&  // Compare les attributs
                Objects.equals(mealTypes, that.mealTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jour, mealTypes);  // Génère un hash basé sur les attributs
    }


}

