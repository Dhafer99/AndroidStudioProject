package com.example.mobile.ui.food.Plan.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.database.FoodEntity;
import com.example.mobile.dto.MealTypeFood;

import java.util.List;
public class MealTypeAdapter extends RecyclerView.Adapter<MealTypeAdapter.MealTypeViewHolder> {

    private List<MealTypeFood> mealTypeFoods;

    public MealTypeAdapter(List<MealTypeFood> mealTypeFoods) {
        this.mealTypeFoods = mealTypeFoods;
    }

    @NonNull
    @Override
    public MealTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_type, parent, false);
        return new MealTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealTypeViewHolder holder, int position) {
        MealTypeFood mealTypeFood = mealTypeFoods.get(position);
        holder.mealTypeTextView.setText(mealTypeFood.getMealType());

        // Set food items
        StringBuilder foodInfo = new StringBuilder();
        for (FoodEntity food : mealTypeFood.getFoodItems()) {
            foodInfo.append(food.getNom()).append("\n");
        }
        holder.foodItemsTextView.setText(foodInfo.toString());
    }

    @Override
    public int getItemCount() {
        return mealTypeFoods.size();
    }

    public static class MealTypeViewHolder extends RecyclerView.ViewHolder {
        TextView mealTypeTextView;
        TextView foodItemsTextView;

        public MealTypeViewHolder(View itemView) {
            super(itemView);
            mealTypeTextView = itemView.findViewById(R.id.textViewMealType);
            foodItemsTextView = itemView.findViewById(R.id.textViewFoodItems);
        }
    }
}



