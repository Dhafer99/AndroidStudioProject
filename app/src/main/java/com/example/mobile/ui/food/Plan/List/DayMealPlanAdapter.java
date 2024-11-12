package com.example.mobile.ui.food.Plan.List;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.database.FoodEntity;
import com.example.mobile.dto.DayMealPlan;
import com.example.mobile.dto.MealTypeFood;
import com.example.mobile.ui.food.Product.List.FoodAdapter;

import java.util.List;

public class DayMealPlanAdapter extends RecyclerView.Adapter<DayMealPlanAdapter.DayMealPlanViewHolder> {

    private List<DayMealPlan> dayMealPlans;
    private OnPlanDeleteListener deleteListener;
    private Context context;
    private OnItemDeleteListener onItemDeleteListener;

    public interface OnPlanDeleteListener {
        void onDelete(DayMealPlan plan);
    }

    public DayMealPlanAdapter(List<DayMealPlan> dayMealPlans, OnItemDeleteListener listener) {
        this.dayMealPlans = dayMealPlans;
        this.onItemDeleteListener = listener;
        this.context = context;
    }

    public void updateData(List<DayMealPlan> newData) {
        this.dayMealPlans = newData;
        notifyDataSetChanged(); // Notify adapter that the data has changed
    }

    @NonNull
    @Override
    public DayMealPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_meal_plan, parent, false);
        return new DayMealPlanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DayMealPlanViewHolder holder, int position) {
        DayMealPlan dayMealPlan = dayMealPlans.get(position);
        holder.dayTextView.setText(dayMealPlan.getJour() != null ? dayMealPlan.getJour() : "");

        MealTypeAdapter mealTypeAdapter = new MealTypeAdapter(dayMealPlan.getMealTypes());
        holder.recyclerViewMealTypes.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerViewMealTypes.setAdapter(mealTypeAdapter);

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // If confirmed, call the delete listener
                        onItemDeleteListener.onItemDelete(dayMealPlan);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });

        // Iterate through meal types and foods, binding data as needed
        /*StringBuilder mealInfo = new StringBuilder();
        for (MealTypeFood mealTypeFood : dayMealPlan.getMealTypes()) {
            mealInfo.append(mealTypeFood.getMealType()).append(": ");
            for (FoodEntity food : mealTypeFood.getFoodItems()) {
                mealInfo.append(food.getNom()).append(", ");
            }
            mealInfo.append("\n");
        }
        holder.mealInfoTextView.setText(mealInfo.toString());*/
    }

    @Override
    public int getItemCount() {
        return dayMealPlans.size();
    }

    public static class DayMealPlanViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;
        RecyclerView recyclerViewMealTypes;
        Button deleteButton;// Inner RecyclerView for meal types

        public DayMealPlanViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.textViewDay);
            recyclerViewMealTypes = itemView.findViewById(R.id.recyclerViewMealTypes);
            deleteButton = itemView.findViewById(R.id.btnDeletePlan);
        }
    }

    public interface OnItemDeleteListener {
        void onItemDelete(DayMealPlan dayMealPlan);
    }
}
