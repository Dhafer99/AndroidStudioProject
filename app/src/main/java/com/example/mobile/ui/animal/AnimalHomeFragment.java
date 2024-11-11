package com.example.mobile.ui.animal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.mobile.R;

public class AnimalHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_animal_home, container, false);

        // Initialize UI components
        Button buttonAddAnimal = view.findViewById(R.id.buttonAddAnimal);
        Button buttonAnimalList = view.findViewById(R.id.buttonAnimalList);

        // Set button click listeners
        buttonAddAnimal.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_animalHomeFragment_to_animalFragment);
        });

        buttonAnimalList.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_animalHomeFragment_to_animalListFragment);
        });

        return view;
    }
}
