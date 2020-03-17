package com.example.myapplication.admin;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.example.myapplication.adapters.MyServicesRecyclerViewAdapter;
import com.example.myapplication.models.ServicesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ServicesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private ArrayList<ServicesModel> servicesModelArrayList;

    //Creating an instance
    FirebaseDatabase database;
    DatabaseReference servicesDatabase;
    AdminServicesAdapter adminServicesAdapter;
    RecyclerView servicesRecycler;
    TextView nothingLoadedServices;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ServicesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ServicesFragment newInstance(int columnCount) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        servicesModelArrayList = new ArrayList<>();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services_list, container, false);

        FloatingActionButton ab = view.findViewById(R.id.fab_add_service);

        final AddServiceDialog addServiceDialog = new AddServiceDialog();
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addServiceDialog.showDialog(getActivity(), getContext());
            }
        });
        initView(view);

        // Set the adapter
      /*  if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyServicesRecyclerViewAdapter(servicesModelArrayList, mListener));
        }*/
        return view;
    }

    private void initView(View view) {
        database = FirebaseDatabase.getInstance();
        servicesDatabase = database.getReference("services");
        servicesRecycler = view.findViewById(R.id.recycler_list_services);
        nothingLoadedServices = view.findViewById(R.id.nothing_loaded_service);

        servicesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        servicesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                servicesModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ServicesModel serviceModel = new ServicesModel();
                    serviceModel.setServiceId(dataSnapshot1.getKey());
                    Log.d("INJECTING", dataSnapshot1.getValue().toString());
                    serviceModel.setServiceName(dataSnapshot1.getValue().toString());
                    servicesModelArrayList.add(serviceModel);
                }
                adminServicesAdapter = new AdminServicesAdapter(getContext(), servicesModelArrayList);
                servicesRecycler.setAdapter(adminServicesAdapter);
                adminServicesAdapter.notifyDataSetChanged();
                if (servicesModelArrayList.isEmpty())
                    nothingLoadedServices.setVisibility(View.VISIBLE);
                else
                    nothingLoadedServices.setVisibility(View.GONE);
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
        void onListFragmentInteraction(ServicesModel item);
    }
}
