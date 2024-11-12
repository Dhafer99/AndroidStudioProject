package com.example.mobile.ui.food.Product.List;


import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile.R;
import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.repositories.FoodRepository;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodEntity> foodList;
    private OnItemDeleteListener onItemDeleteListener;
    private FoodRepository foodRepository;
    private Context context;

    // Constructor
    public FoodAdapter(List<FoodEntity> foodList, OnItemDeleteListener listener, FoodRepository foodRepository, Context context) {
        this.foodList = foodList;
        this.onItemDeleteListener = listener;
        this.foodRepository = foodRepository;
        this.context = context;
    }

    public void setFoodList(List<FoodEntity> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodEntity food = foodList.get(position);
        holder.foodName.setText(food.getNom());
        holder.foodDescription.setText(food.getDescription());
        holder.foodType.setText(food.getType());

        Glide.with(holder.itemView.getContext())
                .load(food.getImage()) // Replace with your image source (e.g., URL or file path)
                .placeholder(R.drawable.baseline_hide_image_24) // Optional placeholder
                .into(holder.foodImageView);

        holder.itemView.setOnLongClickListener(v -> {
            showEditDialog(food);
            return true;
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Display confirmation dialog
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // If confirmed, call the delete listener
                        onItemDeleteListener.onItemDelete(food);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });
    }

    private void showEditDialog(FoodEntity food) {
        // Create an EditText to capture new food name, description, and type
        EditText foodNameEditText = new EditText(context);
        EditText foodDescriptionEditText = new EditText(context);
        AutoCompleteTextView foodTypeEditText = new AutoCompleteTextView(context);

        // Set the current values in the EditTexts
        foodNameEditText.setText(food.getNom());
        foodDescriptionEditText.setText(food.getDescription());
        foodTypeEditText.setText(food.getType());

        String[] optionsType = {"Product", "Recipe"};

        // Create an ArrayAdapter for the AutoCompleteTextView
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, optionsType);
        foodTypeEditText.setAdapter(adapterType);

        foodTypeEditText.setOnClickListener(v -> foodTypeEditText.showDropDown());

        // Create and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Food");

        // Set the view for the dialog
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(foodNameEditText);
        layout.addView(foodDescriptionEditText);
        layout.addView(foodTypeEditText);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Retrieve the updated values
            String newFoodName = foodNameEditText.getText().toString();
            String newFoodDescription = foodDescriptionEditText.getText().toString();
            String newFoodType = foodTypeEditText.getText().toString();

            // Update the FoodEntity object with new data
            food.setNom(newFoodName);
            food.setDescription(newFoodDescription);
            food.setType(newFoodType);

            // Call the method to update the database using FoodRepository
            foodRepository.updateFood(food);

            // Optionally, update the list and notify the adapter
            notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodDescription, foodType;
        ImageButton btnDelete, btnEdit;
        ImageView foodImageView;

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodType = itemView.findViewById(R.id.food_type);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            foodImageView = itemView.findViewById(R.id.food_image);
        }
    }

    public interface OnItemDeleteListener {
        void onItemDelete(FoodEntity food);
    }
}
