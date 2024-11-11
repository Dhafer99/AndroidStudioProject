package com.example.mobile.ui.animal;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mobile.R;
import com.example.mobile.database.AnimalEntity;
import com.squareup.picasso.Picasso;

public class AnimalDetailsFragment extends Fragment {

    private ImageView imageViewAnimalDetails;
    private TextView textViewAnimalName, textViewAnimalSpecies, textViewAnimalAge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_animal_details, container, false);

        // Initialize UI components
        imageViewAnimalDetails = view.findViewById(R.id.imageViewAnimalDetails);
        textViewAnimalName = view.findViewById(R.id.textViewAnimalName);
        textViewAnimalSpecies = view.findViewById(R.id.textViewAnimalSpecies);
        textViewAnimalAge = view.findViewById(R.id.textViewAnimalAge);

        // Get the passed AnimalEntity from arguments
        if (getArguments() != null) {
            AnimalEntity selectedAnimal = getArguments().getParcelable("animal");
            if (selectedAnimal != null) {
                // Populate the fields with the animal data
                textViewAnimalName.setText(selectedAnimal.getName() != null ? selectedAnimal.getName() : "Unknown");
                textViewAnimalSpecies.setText("Species: " + (selectedAnimal.getSpecies() != null ? selectedAnimal.getSpecies() : "Unknown"));
                textViewAnimalAge.setText("Age: " + selectedAnimal.getAge());

                // Load the image using Picasso, with a placeholder if imageUri is null
                if (selectedAnimal.getImageUri() != null) {
                    Uri imageUri = Uri.parse(selectedAnimal.getImageUri());
                    Picasso.get().load(imageUri).placeholder(R.drawable.placeholder_image).into(imageViewAnimalDetails);
                } else {
                    Picasso.get().load(R.drawable.placeholder_image).into(imageViewAnimalDetails);
                }
            }
        }

        return view;
    }
}
