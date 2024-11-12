package com.example.mobile.Adapters;

// UserAdapter.java
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserEntity> users;

    public UserAdapter(List<UserEntity> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserEntity user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private Button btnView, btnBlock, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textUserName);
            btnView = itemView.findViewById(R.id.btnView);
            btnBlock = itemView.findViewById(R.id.btnBlock);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(UserEntity user) {
            userName.setText(user.getName());
            // View User Details
            btnView.setOnClickListener(v -> showUserDetails(user));

            // Block/Unblock User
            btnBlock.setOnClickListener(v -> toggleBlockStatus(user));



            btnDelete.setOnClickListener(v -> {
                // Code to delete user
            });
        }

        private void showUserDetails(UserEntity user) {
            // Show user details in an AlertDialog
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle("User Details")
                    .setMessage("Name: " + user.getName() + "\nEmail: " + user.getEmail() +
                            "\nPhone: " + user.getPhoneNumber() + "\nRole: " + user.getRole())
                    .setPositiveButton("OK", null)
                    .show();
        }


        private void toggleBlockStatus(UserEntity user) {
            boolean isBlocked = !user.getIsBlocked();
            user.setIsBlocked(isBlocked);
            notifyItemChanged(getAdapterPosition());

            String status = isBlocked ? "blocked" : "unblocked";
            Toast.makeText(itemView.getContext(), "User " + user.getName() + " has been " + status, Toast.LENGTH_SHORT).show();

            // Update the block status in the database
            Executors.newSingleThreadExecutor().execute(() -> {
                UserRepository userRepository = new UserRepository(itemView.getContext());
                userRepository.updateUserBlockStatus(user.getUserId(), isBlocked);
            });
        }

    }



}
