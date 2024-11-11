package com.example.mobile.ui.animal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.mobile.R;
import com.example.mobile.database.AnimalEntity;

public class AnimalFragment extends Fragment {

    private EditText editTextName, editTextSpecies, editTextAge;
    private Button buttonSave;
    private AnimalViewModel animalViewModel;
    private int userId = 1; // Replace this with the actual user ID

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_animal, container, false);

        // Initialize UI components
        editTextName = view.findViewById(R.id.editTextName);
        editTextSpecies = view.findViewById(R.id.editTextSpecies);
        editTextAge = view.findViewById(R.id.editTextAge);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Initialize ViewModel
        animalViewModel = new ViewModelProvider(this).get(AnimalViewModel.class);

        // Set button click listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnimal(v);
            }
        });

        return view;
    }

    private void saveAnimal(View view) {
        String name = editTextName.getText().toString().trim();
        String species = editTextSpecies.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || species.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter a valid age", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new AnimalEntity and insert it using the ViewModel
        AnimalEntity newAnimal = new AnimalEntity();
        newAnimal.setName(name);
        newAnimal.setSpecies(species);
        newAnimal.setAge(age);

        animalViewModel.insert(newAnimal, userId);
        Toast.makeText(requireContext(), "Animal added successfully", Toast.LENGTH_SHORT).show();

        // Clear input fields after saving
        editTextName.setText("");
        editTextSpecies.setText("");
        editTextAge.setText("");

        // Navigate to the display fragment to show the list of animals
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_animalFragment_to_animalListFragment);
    }
}
