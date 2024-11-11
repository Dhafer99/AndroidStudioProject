package com.example.mobile.ui.animal;

import android.content.Context;
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
        holder.textViewName.setText("Name: " + animal.getName());
        holder.textViewSpecies.setText("Species: " + animal.getSpecies());
        holder.textViewAge.setText("Age: " + animal.getAge());

        // Set click listener for edit icon to navigate to AnimalEditFragment
        holder.iconEdit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("animal", animal); // Ensure AnimalEntity implements Parcelable
            Navigation.findNavController(v).navigate(R.id.action_animalListFragment_to_animalEditFragment, bundle);
        });

        // Set click listener for delete icon
        holder.iconDelete.setOnClickListener(v -> actionListener.onDelete(animal));
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewSpecies, textViewAge;
        ImageView iconEdit, iconDelete;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewSpecies = itemView.findViewById(R.id.textViewSpecies);
            textViewAge = itemView.findViewById(R.id.textViewAge);
            iconEdit = itemView.findViewById(R.id.iconEdit);
            iconDelete = itemView.findViewById(R.id.iconDelete);
        }
    }
}
