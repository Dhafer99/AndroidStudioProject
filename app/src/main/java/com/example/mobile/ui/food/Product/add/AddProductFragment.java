package com.example.mobile.ui.food.Product.add;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobile.R;
import com.example.mobile.database.FoodEntity;
import com.example.mobile.databinding.FragmentAddproductBinding;

public class AddProductFragment extends Fragment {

    private FragmentAddproductBinding binding;
    private ImageView foodImageView;
    private static final int IMAGE_PICK_CODE = 1000;
    private Uri selectedImageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddProductViewModel addProductViewModel = new ViewModelProvider(this).get(AddProductViewModel.class);

        binding = FragmentAddproductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AutoCompleteTextView dropdownType = root.findViewById(R.id.dropdown_typeproduct);
        String[] optionsType = {"Product", "Recipe"};
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, optionsType);
        dropdownType.setAdapter(adapterType);
        dropdownType.setOnClickListener(v -> dropdownType.showDropDown());

        EditText foodNameEditText = binding.etName;
        EditText foodDescriptionEditText = binding.etDescription;
        Button saveButton = root.findViewById(R.id.addProduct_btn);
        foodImageView = root.findViewById(R.id.food_image_view);
        Button uploadImageButton = root.findViewById(R.id.upload_image_button);

        uploadImageButton.setOnClickListener(v -> openImagePicker());

        saveButton.setOnClickListener(v -> {
            String name = foodNameEditText.getText().toString().trim();
            String description = foodDescriptionEditText.getText().toString().trim();
            String selectedType = dropdownType.getText().toString().trim();

            if (name.isEmpty()) {
                foodNameEditText.setError("Please enter a name for the food");
                return;
            }

            if (description.isEmpty()) {
                foodDescriptionEditText.setError("Please enter a description");
                return;
            }

            if (selectedType.isEmpty()) {
                dropdownType.setError("Please select a type");
                return;
            }

            FoodEntity foodEntity = new FoodEntity();
            foodEntity.setNom(name);
            foodEntity.setDescription(description);
            foodEntity.setType(selectedType);

            if (selectedImageUri != null) {
                foodEntity.setImage(selectedImageUri.toString()); // Store the image URI
            }

            addProductViewModel.insertFood(foodEntity);
            Toast.makeText(requireContext(), "Food item added", Toast.LENGTH_SHORT).show();
            foodNameEditText.setText("");
            foodDescriptionEditText.setText("");
            dropdownType.setText("");
            foodImageView.setImageResource(R.drawable.baseline_hide_image_24); // Reset the image
            selectedImageUri = null;
        });

        return root;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == requireActivity().RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            selectedImageUri = data.getData();
            foodImageView.setImageURI(selectedImageUri);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
