package com.example.mobile.ui.vet_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile.R;
import com.example.mobile.database.UserEntity;

import java.util.List;

public class VetAdapter extends RecyclerView.Adapter<VetAdapter.VetViewHolder> {

    private final List<UserEntity> vetList;
    private final OnVetClickListener listener;
    private final String userRole;
    private final Context context;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String IMAGE_URI_KEY = "imageUri";

    public interface OnVetClickListener {
        void onViewRouteClick(UserEntity vet);  // For veterinarian role
        void onViewUserClick(UserEntity user);  // For regular user role
    }

    public VetAdapter(Context context, List<UserEntity> vetList, OnVetClickListener listener, String userRole) {
        this.context = context;
        this.vetList = vetList;
        this.listener = listener;
        this.userRole = userRole;
    }

    @NonNull
    @Override
    public VetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_vet_item, parent, false);
        return new VetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VetViewHolder holder, int position) {
        UserEntity user = vetList.get(position);

        // Retrieve the image URI from UserEntity or from shared preferences
        Uri imageUri = getImageUri(user);
        if (imageUri != null) {
            Glide.with(context)
                    .load(imageUri)
                    .placeholder(R.drawable.baseline_hide_image_24) // Optional placeholder
                    .into(holder.userImageView);
        } else {
            // Optionally, load a default image if URI is not available
            holder.userImageView.setImageResource(R.drawable.baseline_hide_image_24);
        }

        holder.bind(user, listener, userRole);
    }

    @Override
    public int getItemCount() {
        return vetList.size();
    }

    // Method to retrieve the image URI
    private Uri getImageUri(UserEntity user) {
        // Try getting the URI from the UserEntity data first
        String imageUriString = user.getImage();

        // If image URI is not null in UserEntity, use it directly
        if (imageUriString != null) {
            return Uri.parse(imageUriString);
        }

        // Otherwise, fall back to the URI stored in shared preferences
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedUriString = prefs.getString(IMAGE_URI_KEY, null);
        return savedUriString != null ? Uri.parse(savedUriString) : null;
    }

    public static class VetViewHolder extends RecyclerView.ViewHolder {
        private final TextView textUserName;
        private final Button btnView;
        ImageView userImageView;

        public VetViewHolder(@NonNull View itemView) {
            super(itemView);
            textUserName = itemView.findViewById(R.id.textUserName);
            btnView = itemView.findViewById(R.id.btnView);
            userImageView = itemView.findViewById(R.id.user_image);
        }

        public void bind(UserEntity user, OnVetClickListener listener, String userRole) {
            textUserName.setText(user.getName());

            // Set button behavior based on the user role
            if ("Veterinarian".equals(userRole)) {
                btnView.setText("View User");
                btnView.setOnClickListener(v -> listener.onViewUserClick(user));
            } else {
                btnView.setText("View Vet Map");
                btnView.setOnClickListener(v -> listener.onViewRouteClick(user));
            }
        }
    }
}
