package com.example.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.Adapters.UserAdapter;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;
import com.example.mobile.ui.login.LoginSignupActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;

    private UserRepository userRepository ;
    private UserAdapter userAdapter; // Custom adapter to handle user data
    private Button backToLogin;

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

        backToLogin = findViewById(R.id.buttonLogout);
        // Set up logout button to navigate to LoginActivity
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, LoginSignupActivity.class);
                startActivity(intent);
                finish(); // Optionally close the current activity to prevent going back
            }
        });


    }

    private List<UserEntity> loadUsers() {
        // Dummy data or fetch from database/API
        return userRepository.getAllUsers(); // This should retrieve your list of users
    }
}