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

public class AnimalEditFragment extends Fragment {

    private EditText editTextName, editTextSpecies, editTextAge;
    private Button buttonSaveChanges;
    private AnimalViewModel animalViewModel;
    private AnimalEntity animalToEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment (using the edit interface layout)
        View view = inflater.inflate(R.layout.fragment_animal_edit, container, false);

        // Initialize UI components
        editTextName = view.findViewById(R.id.editTextName);
        editTextSpecies = view.findViewById(R.id.editTextSpecies);
        editTextAge = view.findViewById(R.id.editTextAge);
        buttonSaveChanges = view.findViewById(R.id.buttonSaveChanges);

        // Initialize ViewModel
        animalViewModel = new ViewModelProvider(requireActivity()).get(AnimalViewModel.class);

        // Get the passed AnimalEntity from arguments (if any)
        if (getArguments() != null) {
            animalToEdit = getArguments().getParcelable("animal"); // Get Parcelable instead of Serializable
            if (animalToEdit != null) {
                // Populate the fields with current animal data
                editTextName.setText(animalToEdit.getName());
                editTextSpecies.setText(animalToEdit.getSpecies());
                editTextAge.setText(String.valueOf(animalToEdit.getAge()));
            }
        }

        // Set button click listener for saving changes
        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAnimal(v);
            }
        });

        return view;
    }

    private void updateAnimal(View view) {
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

        // Update the animal entity
        if (animalToEdit != null) {
            animalToEdit.setName(name);
            animalToEdit.setSpecies(species);
            animalToEdit.setAge(age);
            animalViewModel.update(animalToEdit);
            Toast.makeText(requireContext(), "Animal updated successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to the list after saving changes
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_animalEditFragment_to_animalListFragment);
        }
    }
}
