package com.example.myapplication.admin;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdminServicesAdapter;
import com.example.myapplication.adapters.MyHospitalRecyclerViewAdapter;
import com.example.myapplication.models.ServicesModel;
import com.example.myapplication.models.hospitals;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HospitalFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private ArrayList<hospitals> hospitalsArrayList;

    //Creating an instance
    FirebaseDatabase database;
    DatabaseReference hospitalDatabase;
    MyHospitalRecyclerViewAdapter adminHospitalAdapter;
    RecyclerView hospitalRecycler;
    TextView nothinLoadedHospitals;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HospitalFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HospitalFragment newInstance(int columnCount) {
        HospitalFragment fragment = new HospitalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospitalsArrayList = new ArrayList<>();


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_list, container, false);
        FloatingActionButton ab = view.findViewById(R.id.fab_add_hospital);

        final AddHospitalDialog addHospitalDialog = new AddHospitalDialog();

        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHospitalDialog.showDialog(getActivity(), getContext());
            }
        });

        initView(view);


        return view;
    }

    private void initView(View view) {
        database = FirebaseDatabase.getInstance();
        hospitalDatabase = database.getReference("hospitals");
        hospitalRecycler = view.findViewById(R.id.list_hospital_recycler);
        nothinLoadedHospitals = view.findViewById(R.id.nothing_loaded_hospital);

        hospitalRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        hospitalDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                hospitalsArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    hospitals hospitalModel = new hospitals();
                    hospitalModel.sethId(dataSnapshot1.getKey());
                    //Log.d("SERVICES", dataSnapshot1.child("sericesOfferedList").getChildren());
                    hospitalModel.setName(dataSnapshot1.child("name").getValue().toString());
                    List<ServicesModel> loadedServices = new ArrayList<>();

                    for (DataSnapshot service : dataSnapshot1.child("servicesOfferedList").getChildren()) {
                        ServicesModel offered = new ServicesModel();
                        offered.setServiceName(service.child("serviceName").getValue().toString());
                        loadedServices.add(offered);

                    }
                    hospitalModel.setServicesOfferedList(loadedServices);
                    hospitalModel.setKephLevel(dataSnapshot1.child("kephLevel").getValue().toString());
                    hospitalModel.setLat(Double.parseDouble(dataSnapshot1.child("lat").getValue().toString()));
                    hospitalModel.setLongi(Double.parseDouble(dataSnapshot1.child("longi").getValue().toString()));

                    hospitalsArrayList.add(hospitalModel);
                }
                adminHospitalAdapter = new MyHospitalRecyclerViewAdapter(hospitalsArrayList, mListener);
                hospitalRecycler.setAdapter(adminHospitalAdapter);
                adminHospitalAdapter.notifyDataSetChanged();
                if (hospitalsArrayList.isEmpty())
                    nothinLoadedHospitals.setVisibility(View.VISIBLE);
                else
                    nothinLoadedHospitals.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(hospitals item);
    }
}
