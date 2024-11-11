package com.example.mobile.ui.vet_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
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

        // Fetch vets from the repository and set up the adapter
        userRepository = new UserRepository(requireContext());
        try {
            loadVetList();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    private void loadVetList() throws ExecutionException, InterruptedException {

        Future<List<UserEntity>> futureVets = userRepository.getVeterinarianUsers();
        List<UserEntity> vets = futureVets.get();

        // Ensure this method returns only vets
        VetAdapter adapter = new VetAdapter(vets, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewRouteClick(UserEntity vet) {
        // Open map with route to selected vet
        VetMapFragment vetMapFragment = new VetMapFragment();
        Bundle args = new Bundle();
        args.putDouble("lat", vet.getLatitude());
        args.putDouble("lng", vet.getLongitude());

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.nav_vet, args);
    }
}
