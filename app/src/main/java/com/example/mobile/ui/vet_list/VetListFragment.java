package com.example.mobile.ui.vet_list;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.Session.SessionManager;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;
import com.example.mobile.ui.vet_map.VetMapFragment;
import androidx.navigation.NavController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class VetListFragment extends Fragment implements VetAdapter.OnVetClickListener {

    private RecyclerView recyclerView;
    private UserRepository userRepository;

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

        return view;
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
            VetAdapter adapter = new VetAdapter(vets, this, sessionManager.getUserRole());
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
            VetAdapter adapter = new VetAdapter(users, this, sessionManager.getUserRole());
            recyclerView.setAdapter(adapter);
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
