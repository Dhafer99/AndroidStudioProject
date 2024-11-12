package com.example.mobile.ui.animal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private Button buttonSave, buttonSelectImage;
    private ImageView imageViewAnimal;
    private AnimalViewModel animalViewModel;
    private int userId = 1; // Replace this with the actual user ID
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;
    private Uri imageUri;

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
        imageViewAnimal = view.findViewById(R.id.imageViewAnimal);
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage);

        // Initialize ViewModel
        animalViewModel = new ViewModelProvider(this).get(AnimalViewModel.class);

        // Set button click listener
        buttonSave.setOnClickListener(v -> saveAnimal(v));

        buttonSelectImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                // Permission already granted
                openGallery();
            }
        });

        // Preloaded image click listeners
        ImageView preloadedImage1 = view.findViewById(R.id.preloadedImage1);
        ImageView preloadedImage2 = view.findViewById(R.id.preloadedImage2);
        ImageView preloadedImage3 = view.findViewById(R.id.preloadedImage3);

        preloadedImage1.setOnClickListener(v -> {
            imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.emhopper);
            imageViewAnimal.setImageURI(imageUri); // Display the selected image
        });

        preloadedImage2.setOnClickListener(v -> {
            imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.pixabay);
            imageViewAnimal.setImageURI(imageUri); // Display the selected image
        });

        preloadedImage3.setOnClickListener(v -> {
            imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.caio);
            imageViewAnimal.setImageURI(imageUri); // Display the selected image
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

        // Save the image URI if available
        if (imageUri != null) {
            newAnimal.setImageUri(imageUri.toString());
        }

        animalViewModel.insert(newAnimal, userId);
        Toast.makeText(requireContext(), "Animal added successfully", Toast.LENGTH_SHORT).show();

        // Clear input fields after saving
        editTextName.setText("");
        editTextSpecies.setText("");
        editTextAge.setText("");
        imageViewAnimal.setImageURI(null);

        // Navigate to the display fragment to show the list of animals
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_animalFragment_to_animalListFragment);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewAnimal.setImageURI(imageUri); // Display the selected image
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(requireContext(), "Permission denied to access storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
