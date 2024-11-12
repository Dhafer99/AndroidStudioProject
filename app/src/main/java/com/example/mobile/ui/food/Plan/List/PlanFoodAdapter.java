package com.example.mobile.ui.food.Plan.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.dto.DayMealPlan;

import java.util.List;

public class PlanFoodAdapter extends RecyclerView.Adapter<PlanFoodAdapter.PlanFoodViewHolder> {
    private List<DayMealPlan> dayMealPlanList;

    public PlanFoodAdapter(List<DayMealPlan> dayMealPlanList) {
        this.dayMealPlanList = dayMealPlanList;
    }

    @NonNull
    @Override
    public PlanFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new PlanFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanFoodViewHolder holder, int position) {
        DayMealPlan dayMealPlan = dayMealPlanList.get(position);
        holder.bind(dayMealPlan);
    }

    @Override
    public int getItemCount() {
        return dayMealPlanList.size();
    }

    public class PlanFoodViewHolder extends RecyclerView.ViewHolder {
        private TextView jourTextView;
        private RecyclerView mealTypeRecyclerView;

        public PlanFoodViewHolder(View itemView) {
            super(itemView);
            jourTextView = itemView.findViewById(R.id.plan_day);
            mealTypeRecyclerView = itemView.findViewById(R.id.recycler_view_meal_types);
        }

        public void bind(DayMealPlan dayMealPlan) {
            jourTextView.setText(dayMealPlan.getJour());

            // Set up RecyclerView for meal types (Breakfast, Dinner, etc.)
            MealTypeAdapter mealTypeAdapter = new MealTypeAdapter(dayMealPlan.getMealTypes());
            mealTypeRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            mealTypeRecyclerView.setAdapter(mealTypeAdapter);
        }
    }
}


