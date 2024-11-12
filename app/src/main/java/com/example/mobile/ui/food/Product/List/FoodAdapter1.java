package com.example.mobile.ui.food.Product.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.database.FoodEntity;

import java.util.List;

public class FoodAdapter1 extends RecyclerView.Adapter<FoodAdapter1.FoodViewHolder> {
    private List<FoodEntity> foodItems;

    public FoodAdapter1(List<FoodEntity> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food1, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodEntity foodEntity = foodItems.get(position);
        holder.bind(foodEntity);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView foodNameTextView;

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.food_name_text);
        }

        public void bind(FoodEntity foodEntity) {
            foodNameTextView.setText(foodEntity.getNom());
        }
    }
}

