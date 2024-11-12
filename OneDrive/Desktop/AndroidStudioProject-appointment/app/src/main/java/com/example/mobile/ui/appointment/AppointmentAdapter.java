package com.example.mobile.ui.appointment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.database.AppointmentEntity;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>{

    private final List<AppointmentEntity> appointmentList;
    private final Context context;
    private final AppointmentAdapter.OnAppointmentActionListener actionListener;

    public interface OnAppointmentActionListener {
        void onEdit(AppointmentEntity appointment);
        void onDelete(AppointmentEntity appointment);
    }

    public AppointmentAdapter(List<AppointmentEntity> appointmentList, Context context, OnAppointmentActionListener actionListener) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.actionListener = actionListener;
    }



    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new  AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentEntity appointment = appointmentList.get(position);
        holder.textViewDate.setText("Date: " + appointment.getDate());
        holder.textViewPlace.setText("Place: " + appointment.getPlace());
        holder.textViewDuration.setText("Duration: " + appointment.getDuration());

        // Set click listener for edit icon to navigate to AppointmentEditFragment
        holder.iconEdit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("appointment", appointment); // Pass as Serializable
            Navigation.findNavController(v).navigate(R.id.action_appointmentListFragment_to_appointmentEditFragment, bundle);
        });

        // Set click listener for delete icon
        holder.iconDelete.setOnClickListener(v -> actionListener.onDelete(appointment));
    }


    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewPlace, textViewDuration;
        ImageView iconEdit, iconDelete;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewPlace = itemView.findViewById(R.id.textViewPlace);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
            iconEdit = itemView.findViewById(R.id.iconEdit);
            iconDelete = itemView.findViewById(R.id.iconDelete);
        }
    }
}
