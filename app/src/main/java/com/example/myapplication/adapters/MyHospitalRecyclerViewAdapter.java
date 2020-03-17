package com.example.myapplication.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.admin.HospitalFragment.OnListFragmentInteractionListener;
import com.example.myapplication.models.hospitals;

import java.util.List;

/*
Recycles list of hospitals -- instead of building an exhaustive list, data is only built when its almost being
displayed and removed when it exits the screen in scrolling manner.

 */

public class MyHospitalRecyclerViewAdapter extends RecyclerView.Adapter<MyHospitalRecyclerViewAdapter.ViewHolder> {

    private final List<hospitals> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyHospitalRecyclerViewAdapter(List<hospitals> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hospital, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mHospitalName.setText(mValues.get(position).getName());
        holder.mNumberOfServicesOffered.setText(mValues.get(position).getServicesOfferedCount());
        holder.mKephLevel.setText(mValues.get(position).getKephLevel());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(mValues.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mHospitalName;
        public final TextView mNumberOfServicesOffered;
        private final TextView mKephLevel;
        public hospitals mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHospitalName = (TextView) view.findViewById(R.id.hospital_name);
            mNumberOfServicesOffered = (TextView) view.findViewById(R.id.services_offered_count);
            mKephLevel = view.findViewById(R.id.keph_level);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNumberOfServicesOffered.getText() + "'";
        }
    }
}
