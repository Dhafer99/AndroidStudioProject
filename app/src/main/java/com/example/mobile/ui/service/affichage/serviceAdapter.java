package com.example.mobile.ui.service.affichage;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.database.ServiceEntity;

import java.util.ArrayList;
import java.util.List;

public class serviceAdapter extends RecyclerView.Adapter<serviceAdapter.MyviewHolder> {

    private Context context;
    private List<ServiceEntity> serviceList;

    public serviceAdapter(Context context) {
        this.context = context;
        serviceList = new ArrayList<>();
    }
    public void addService(ServiceEntity serviceEntity){
        serviceList.add(serviceEntity);
        notifyDataSetChanged();    }

    public void setServiceList(List<ServiceEntity> serviceList) {
        this.serviceList = serviceList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_serviceaffichage, parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        ServiceEntity serviceEntity = serviceList.get(position);
        holder.name.setText(serviceEntity.getName());
        holder.description.setText(serviceEntity.getDescription());
        holder.phone.setText(serviceEntity.getPhone());
        holder.place.setText(serviceEntity.getPlace());
        holder.startDate.setText(serviceEntity.getStartDate());
        holder.endDate.setText(serviceEntity.getEndDate());
        holder.price.setText(serviceEntity.getPrice());

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{
        private TextView name ,description,phone,place,startDate,endDate,price;

public MyviewHolder(@NonNull View itemView){

    super(itemView);
    name =itemView.findViewById(R.id.name);
    description =itemView.findViewById(R.id.description);
    phone =itemView.findViewById(R.id.phone);
    place =itemView.findViewById(R.id.place);
    startDate =itemView.findViewById(R.id.startDate);
    endDate =itemView.findViewById(R.id.endDate);
    price =itemView.findViewById(R.id.price);



}

}
}
