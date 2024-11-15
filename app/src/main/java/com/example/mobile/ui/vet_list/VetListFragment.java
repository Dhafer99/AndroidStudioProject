package com.example.mobile.ui.vet_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile.R;
import com.example.mobile.Session.SessionManager;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;
import com.example.mobile.ui.vet_map.VetMapFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class VetListFragment extends Fragment implements VetAdapter.OnVetClickListener {

    private RecyclerView recyclerView;
    private UserRepository userRepository;

    // ActivityResultLauncher for selecting an image from gallery
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();

                    // Take persistable URI permission
                    requireContext().getContentResolver().takePersistableUriPermission(
                            imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Copy the image to internal storage and get a URI for it
                    Uri internalUri = copyImageToInternalStorage(requireContext(), imageUri);

                    // Load the copied image into an ImageView for demonstration
                    if (internalUri != null) {
                        ImageView imageView = getView().findViewById(R.id.user_image); // Make sure the ID exists in your layout
                        Glide.with(this).load(internalUri).into(imageView);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vet_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize UserRepository and load list based on user role
        userRepository = new UserRepository(requireContext());
        try {
            loadList();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // Uncomment if you want to trigger the image picker on fragment load for testing
        // pickImageFromGallery();

        return view;
    }

    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        galleryIntent.setType("image/*");
        galleryIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        imagePickerLauncher.launch(galleryIntent);
    }

    // Method to load list based on user role
    private void loadList() throws ExecutionException, InterruptedException {
        SessionManager sessionManager = new SessionManager(getContext());
        Future<String> userType = userRepository.getUserTypeById(sessionManager.getUserId());
        String ustype = userType.get();

        if ("Veterinarian".equals(ustype)) {
            loadVetList();
        } else {
            loadUserList();
        }
    }

    // Load Veterinarian list
    private void loadVetList() throws ExecutionException, InterruptedException {
        Future<List<UserEntity>> futureVets = userRepository.getVeterinarianUsers();
        List<UserEntity> vets = futureVets.get();

        if (vets == null || vets.isEmpty()) {
            Toast.makeText(getContext(), "No veterinarians found.", Toast.LENGTH_SHORT).show();
        } else {
            SessionManager sessionManager = new SessionManager(getContext());
            VetAdapter adapter = new VetAdapter(getContext(), vets, this, sessionManager.getUserRole());
            recyclerView.setAdapter(adapter);
        }
    }

    // Load regular User list
    private void loadUserList() throws ExecutionException, InterruptedException {
        Future<List<UserEntity>> futureUsers = userRepository.getUserListFuture();
        List<UserEntity> users = futureUsers.get();

        if (users == null || users.isEmpty()) {
            Toast.makeText(getContext(), "No users found.", Toast.LENGTH_SHORT).show();
        } else {
            SessionManager sessionManager = new SessionManager(getContext());
            VetAdapter adapter = new VetAdapter(getContext(), users, this, sessionManager.getUserRole());
            recyclerView.setAdapter(adapter);
        }
    }

    // Utility method to copy the image to internal storage and return a URI
    private Uri copyImageToInternalStorage(Context context, Uri sourceUri) {
        try (InputStream inputStream = context.getContentResolver().openInputStream(sourceUri)) {
            File destinationFile = new File(context.getFilesDir(), "temp_image.jpg");
            try (OutputStream outputStream = new FileOutputStream(destinationFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
            return Uri.fromFile(destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onViewRouteClick(UserEntity vet) {
        // Open map with route to selected vet
        VetMapFragment vetMapFragment = new VetMapFragment();
        Bundle args = new Bundle();
        args.putDouble("lat", vet.getLatitude());
        args.putDouble("lng", vet.getLongitude());
        args.putString("vetname", vet.getName());

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.nav_vet, args);
    }

    @Override
    public void onViewUserClick(UserEntity user) {
        // Display user details in an AlertDialog
        new AlertDialog.Builder(requireContext())
                .setTitle("User Details")
                .setMessage("Name: " + user.getName() + "\nEmail: " + user.getEmail())
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
