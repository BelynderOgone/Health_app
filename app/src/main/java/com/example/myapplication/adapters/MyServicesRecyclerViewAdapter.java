package com.example.myapplication.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.admin.ServicesFragment.OnListFragmentInteractionListener;
import com.example.myapplication.models.ServicesModel;

import java.util.List;

public class MyServicesRecyclerViewAdapter extends RecyclerView.Adapter<MyServicesRecyclerViewAdapter.ViewHolder> {

    private final List<ServicesModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyServicesRecyclerViewAdapter(List<ServicesModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_services, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mServicesName.setText(mValues.get(position).getServiceName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mServicesName;
        public ServicesModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mServicesName = (TextView) view.findViewById(R.id.service_offered);
        }

    }
}
