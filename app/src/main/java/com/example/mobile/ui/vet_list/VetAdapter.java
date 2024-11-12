package com.example.mobile.ui.vet_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.database.UserEntity;
import java.util.List;

public class VetAdapter extends RecyclerView.Adapter<VetAdapter.VetViewHolder> {

    private final List<UserEntity> vetList;
    private final OnVetClickListener listener;
    private final String userRole; // Store the user role to conditionally adjust button behavior

    public interface OnVetClickListener {
        void onViewRouteClick(UserEntity vet);  // For veterinarian role
        void onViewUserClick(UserEntity user);  // For regular user role
    }

    public VetAdapter(List<UserEntity> vetList, OnVetClickListener listener, String userRole) {
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
        UserEntity vet = vetList.get(position);
        holder.bind(vet, listener, userRole);
    }

    @Override
    public int getItemCount() {
        return vetList.size();
    }

    public static class VetViewHolder extends RecyclerView.ViewHolder {
        private final TextView textUserName;
        private final Button btnView;

        public VetViewHolder(@NonNull View itemView) {
            super(itemView);
            textUserName = itemView.findViewById(R.id.textUserName);
            btnView = itemView.findViewById(R.id.btnView);
        }

        public void bind(UserEntity user, OnVetClickListener listener, String userRole) {
            textUserName.setText(user.getName());

            // Set button behavior based on the user role
            if ("Veterinarian".equals(userRole)) {
                btnView.setText("View User");
                btnView.setOnClickListener(v -> listener.onViewUserClick(user)); // Regular user data view
            } else {
                btnView.setText("View Vet Map");
                btnView.setOnClickListener(v -> listener.onViewRouteClick(user)); // Veterinarian route view
            }
        }
    }
}
