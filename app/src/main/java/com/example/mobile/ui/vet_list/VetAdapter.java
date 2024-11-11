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

    public interface OnVetClickListener {
        void onViewRouteClick(UserEntity vet);
    }

    public VetAdapter(List<UserEntity> vetList, OnVetClickListener listener) {
        this.vetList = vetList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new VetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VetViewHolder holder, int position) {
        UserEntity vet = vetList.get(position);
        holder.bind(vet, listener);
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

        public void bind(UserEntity vet, OnVetClickListener listener) {
            textUserName.setText(vet.getName());
            btnView.setOnClickListener(v -> listener.onViewRouteClick(vet));
        }
    }
}
