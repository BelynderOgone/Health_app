package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.ServicesModel;

import java.util.List;

public class AdminServicesAdapter extends RecyclerView.Adapter<AdminServicesAdapter.ItemViewHolder>{
    private List<ServicesModel> servicesModelList;
    private Context mContext;

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_entry_list_item_services,parent,false);
        return new ItemViewHolder(view);
    }

    public AdminServicesAdapter(Context mContext,List<ServicesModel> mServiceModelList) {
        this.mContext=mContext;
        this.servicesModelList = mServiceModelList;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ServicesModel servicesModel= servicesModelList.get(position);
        holder.mTvName.setText(servicesModel.getServiceName());
    }

    @Override
    public int getItemCount() {
        return servicesModelList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mTvName=itemView.findViewById(R.id.service_name_list_item);

        }
    }
}
