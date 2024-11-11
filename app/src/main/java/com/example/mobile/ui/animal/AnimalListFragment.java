package com.example.mobile.ui.animal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.database.AnimalEntity;

import java.util.ArrayList;
import java.util.List;

public class AnimalListFragment extends Fragment implements AnimalAdapter.OnAnimalActionListener {

    private RecyclerView recyclerViewAnimals;
    private AnimalAdapter animalAdapter;
    private AnimalViewModel animalViewModel;
    private List<AnimalEntity> animalList = new ArrayList<>();
    private List<AnimalEntity> filteredAnimalList = new ArrayList<>();
    private int userId = 1; // Replace with the actual user ID logic

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_animal_list, container, false);

        // Initialize the RecyclerView
        recyclerViewAnimals = view.findViewById(R.id.recyclerViewAnimals);
        recyclerViewAnimals.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the adapter with the action listener
        animalAdapter = new AnimalAdapter(getContext(), filteredAnimalList, this);
        recyclerViewAnimals.setAdapter(animalAdapter);

        // Initialize the ViewModel
        animalViewModel = new ViewModelProvider(this).get(AnimalViewModel.class);

        // Observe the list of animals for the current user and update the RecyclerView
        animalViewModel.getUserWithAnimals(userId).observe(getViewLifecycleOwner(), userWithAnimals -> {
            if (userWithAnimals != null && userWithAnimals.animals != null) {
                animalList.clear();
                animalList.addAll(userWithAnimals.animals);
                filteredAnimalList.clear();
                filteredAnimalList.addAll(animalList);
                animalAdapter.notifyDataSetChanged();
            }
        });

        // Initialize the SearchView
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterAnimals(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAnimals(newText);
                return false;
            }
        });

        return view;
    }

    private void filterAnimals(String query) {
        filteredAnimalList.clear();
        if (query.isEmpty()) {
            filteredAnimalList.addAll(animalList);
        } else {
            for (AnimalEntity animal : animalList) {
                if (animal.getName().toLowerCase().contains(query.toLowerCase()) ||
                        animal.getSpecies().toLowerCase().contains(query.toLowerCase()) ||
                        String.valueOf(animal.getAge()).contains(query)) {
                    filteredAnimalList.add(animal);
                }
            }
        }
        animalAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEdit(AnimalEntity animal) {
        // Handle edit functionality here (implementation can be added later)
        Toast.makeText(getContext(), "Edit feature not implemented yet.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelete(AnimalEntity animal) {
        // Show confirmation dialog before deleting
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Animal")
                .setMessage("Are you sure you want to delete this animal?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    animalViewModel.delete(animal);
                    Toast.makeText(getContext(), "Animal deleted successfully.", Toast.LENGTH_SHORT).show();

                    // Refresh the list after deletion
                    animalViewModel.getUserWithAnimals(userId).observe(getViewLifecycleOwner(), userWithAnimals -> {
                        if (userWithAnimals != null && userWithAnimals.animals != null) {
                            animalList.clear();
                            animalList.addAll(userWithAnimals.animals);
                            filterAnimals(""); // Reapply filter to ensure correct list state
                        }
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
