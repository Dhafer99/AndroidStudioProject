package com.example.mobile.ui.animal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.database.AnimalEntity;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    private final List<AnimalEntity> animalList;
    private final Context context;
    private final OnAnimalActionListener actionListener;

    // Define the interface for handling edit and delete actions
    public interface OnAnimalActionListener {
        void onEdit(AnimalEntity animal);
        void onDelete(AnimalEntity animal);
    }

    public AnimalAdapter(Context context, List<AnimalEntity> animalList, OnAnimalActionListener actionListener) {
        this.context = context;
        this.animalList = animalList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        AnimalEntity animal = animalList.get(position);

        // Bind animal data to views
        holder.textViewName.setText("Name: " + animal.getName());
        holder.textViewSpecies.setText("Species: " + animal.getSpecies());
        holder.textViewAge.setText("Age: " + animal.getAge());

        // Set the image if available, otherwise use the placeholder image
        if (animal.getImageUri() != null && !animal.getImageUri().isEmpty()) {
            Uri uri = Uri.parse(animal.getImageUri());
            holder.imageViewAnimal.setImageURI(uri);
        } else {
            holder.imageViewAnimal.setImageResource(R.drawable.placeholder_image); // Use a placeholder image if no image is available
        }

        // Set click listener for edit icon to navigate to AnimalEditFragment
        holder.iconEdit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("animal", animal);
            Navigation.findNavController(v).navigate(R.id.action_animalListFragment_to_animalEditFragment, bundle);
        });

        // Set click listener for delete icon
        holder.iconDelete.setOnClickListener(v -> actionListener.onDelete(animal));

        // Set click listener for the entire item to navigate to AnimalDetailsFragment
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("animal", animal);
            Navigation.findNavController(v).navigate(R.id.action_animalListFragment_to_animalDetailsFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    // ViewHolder class to hold the views for each item
    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewSpecies, textViewAge;
        ImageView iconEdit, iconDelete;
        ImageView imageViewAnimal;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewSpecies = itemView.findViewById(R.id.textViewSpecies);
            textViewAge = itemView.findViewById(R.id.textViewAge);
            imageViewAnimal = itemView.findViewById(R.id.imageViewAnimal);
            iconEdit = itemView.findViewById(R.id.iconEdit);
            iconDelete = itemView.findViewById(R.id.iconDelete);
        }
    }
}
