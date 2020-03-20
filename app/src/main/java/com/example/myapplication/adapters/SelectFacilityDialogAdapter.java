package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.ServicesModel;
import com.example.myapplication.models.hospitals;

import java.util.ArrayList;
import java.util.List;

//This adapter assists in selection of services offered by a hospital

//When new hospital is being added, the admin should select services offered.
public class SelectFacilityDialogAdapter extends RecyclerView.Adapter<SelectFacilityDialogAdapter.ItemViewHolder> {
    private List<hospitals> hospitalsList;
    private Context mContext;

    //When items are selected, store them in this list and update when unchecked.

    private OnBluetoothDeviceClickedListener mBluetoothClickListener;

    public void setOnBluetoothDeviceClickedListener(OnBluetoothDeviceClickedListener l) {
        mBluetoothClickListener = l;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_hospital, parent, false);

        return new ItemViewHolder(view);
    }

    public interface OnBluetoothDeviceClickedListener {
        void onBluetoothDeviceClicked(hospitals selectedHospital);
    }


    public SelectFacilityDialogAdapter(Context mContext, List<hospitals> mHospitalsList) {
        this.mContext = mContext;
        this.hospitalsList = mHospitalsList;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final hospitals hospitalsSelected = hospitalsList.get(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothClickListener != null) {

                    mBluetoothClickListener.onBluetoothDeviceClicked(hospitalsSelected);
                }
            }
        });

        holder.mFacilityName.setText(hospitalsSelected.getName());

        holder.mKephLevel.setText(hospitalsSelected.getKephLevel());

        ArrayList<String> servicesFiltered = new ArrayList<>();
        for (ServicesModel singleList : hospitalsSelected.getServicesOfferedList()) {

            servicesFiltered.add(singleList.getServiceName());
        }

        String concatinated = servicesFiltered.toString().replace(", ", ",").replaceAll("[\\[.\\]]", "");

        holder.mServicesList.setText(concatinated);


    }


    @Override
    public int getItemCount() {
        return hospitalsList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mFacilityName;
        TextView mKephLevel;
        TextView mServicesList;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mFacilityName = itemView.findViewById(R.id.hospital_name);
            mKephLevel = itemView.findViewById(R.id.keph_level);
            mServicesList = itemView.findViewById(R.id.services_offered_count);

        }
    }
}
