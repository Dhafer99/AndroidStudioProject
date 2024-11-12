package com.example.mobile.ui.service.affichage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.database.ServiceEntity;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyviewHolder> {

    private Context context;
    private List<ServiceEntity> serviceList;
    private OnServiceActionListener listener;

    public interface OnServiceActionListener {
        void onEdit(ServiceEntity service);  // Callback for Edit action
        void onDelete(ServiceEntity service);

    }

    public ServiceAdapter(Context context, OnServiceActionListener listener) {
        this.context = context;
        this.listener = listener;
        serviceList = new ArrayList<>();
    }

    public void setServiceList(List<ServiceEntity> serviceList) {
        this.serviceList = serviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_serviceaffichage, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        ServiceEntity serviceEntity = serviceList.get(position);

        // Bind data to views
        holder.name.setText(serviceEntity.getName());
        holder.description.setText(serviceEntity.getDescription());
        holder.phone.setText(serviceEntity.getPhone());
        holder.place.setText(serviceEntity.getPlace());
        holder.startDate.setText(serviceEntity.getStartDate());
        holder.endDate.setText(serviceEntity.getEndDate());
        holder.price.setText(serviceEntity.getPrice());

        // Set up the Edit button click listener
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(serviceEntity);  // Trigger the onEdit callback
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(serviceEntity);  // Trigger the onEdit callback
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        private TextView name, description, phone, place, startDate, endDate, price;
        private ImageButton editButton,deleteButton;  // Button for editing

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            phone = itemView.findViewById(R.id.phone);
            place = itemView.findViewById(R.id.place);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            price = itemView.findViewById(R.id.price);
            editButton = itemView.findViewById(R.id.edit_button);  // Initialize Edit button
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
