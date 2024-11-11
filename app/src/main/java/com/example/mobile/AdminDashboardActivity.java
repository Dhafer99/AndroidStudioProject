package com.example.mobile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.Adapters.UserAdapter;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;

    private UserRepository userRepository ;
    private UserAdapter userAdapter; // Custom adapter to handle user data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        // Initialize UserRepository with context
        userRepository = new UserRepository(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            List<UserEntity> users = userRepository.getAllUsers();
            runOnUiThread(() -> {
                userAdapter = new UserAdapter(users);
                recyclerViewUsers.setAdapter(userAdapter);
            });
        });

    }

    private List<UserEntity> loadUsers() {
        // Dummy data or fetch from database/API
        return userRepository.getAllUsers(); // This should retrieve your list of users
    }
}