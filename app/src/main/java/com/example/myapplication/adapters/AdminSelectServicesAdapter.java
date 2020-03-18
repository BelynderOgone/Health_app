package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.ServicesModel;

import java.util.ArrayList;
import java.util.List;

//This adapter assists in selection of services offered by a hospital

//When new hospital is being added, the admin should select services offered.
public class AdminSelectServicesAdapter extends RecyclerView.Adapter<AdminSelectServicesAdapter.ItemViewHolder> {
    private List<ServicesModel> servicesModelList;
    private Context mContext;

    //When items are selected, store them in this list and update when unchecked.
    private List<String> selectedServicesForHospital;

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_single_entry_to_select_offered_service, parent, false);
        return new ItemViewHolder(view);
    }

    public List<String> getSelectedServicesForHospital() {
            return selectedServicesForHospital;
    }

    public AdminSelectServicesAdapter(Context mContext, List<ServicesModel> mServiceModelList) {
        this.mContext = mContext;
        this.servicesModelList = mServiceModelList;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ServicesModel servicesModel = servicesModelList.get(position);
        holder.mTvName.setText(servicesModel.getServiceName());
        selectedServicesForHospital = new ArrayList<>();

        holder.mSelectedService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedServicesForHospital.add(servicesModel.getServiceName());
                } else {
                    selectedServicesForHospital.remove(servicesModel.getServiceName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return servicesModelList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        CheckBox mSelectedService;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.service_name_list_item_select);
            mSelectedService = itemView.findViewById(R.id.select_offered_service_checkbox);

        }
    }
}
