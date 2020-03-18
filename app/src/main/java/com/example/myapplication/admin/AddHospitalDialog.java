package com.example.myapplication.admin;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdminSelectServicesAdapter;
import com.example.myapplication.models.ServicesModel;
import com.example.myapplication.models.hospitals;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddHospitalDialog {
    //This is launched to add a new hospital, fires a dialog with input

    private ArrayList<ServicesModel> servicesModelArrayList;

    //Creating an instance
    private FirebaseDatabase database;
    private DatabaseReference servicesDatabase;
    private AdminSelectServicesAdapter adminServicesAdapter;
    TextView nothingLoaded;
    private RecyclerView servicesListRecycler;
    private String selectedKephLevel = "";

    private EditText hospitalNameEntry;
    private EditText hospitalLat;
    private EditText hospitalLng;

    private hospitals newHospital;

    public void showDialog(Activity activity, final Context context) {
        servicesModelArrayList = new ArrayList<>();

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_add_hospital);

        //Initialising views to be used in loading services list
        nothingLoaded = dialog.findViewById(R.id.services_not_loaded_in_add_hospital);
        servicesListRecycler = dialog.findViewById(R.id.services_list_recycler);

        hospitalNameEntry = dialog.findViewById(R.id.hsp_name);
        hospitalLat = dialog.findViewById(R.id.hsp_lat);
        hospitalLng = dialog.findViewById(R.id.hsp_logi);

        //Preparing selection spinner for KEPH
        final List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Please select a level.");

        spinnerArray.add("KEPH level 1");
        spinnerArray.add("KEPH level 2");
        spinnerArray.add("KEPH level 3");
        spinnerArray.add("KEPH level 4");
        spinnerArray.add("KEPH level 5");
        spinnerArray.add("KEPH level 6");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = dialog.findViewById(R.id.keph_level_spinner);
        sItems.setAdapter(adapter);


        //Listening for selection changes on keph level spinner
        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerArray.get(position).equals("Please select a level.")) {
                    selectedKephLevel = "";
                } else {
                    selectedKephLevel = spinnerArray.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedKephLevel = "";
            }
        });

        //function to populate services list.
        loadServicesList(servicesListRecycler, nothingLoaded, context);


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_save);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On saving, checking all fields/Validation!
                if (TextUtils.isEmpty(hospitalNameEntry.getText()) || hospitalNameEntry.getText().length() < 5) {
                    hospitalNameEntry.setError("Name needs to be at least 5 chars");
                    return;
                }
                if (TextUtils.isEmpty(hospitalLat.getText()) || Double.parseDouble(hospitalLat.getText().toString()) > 2.0 || Double.parseDouble(hospitalLat.getText().toString()) < -1.0) {
                    //K. West is not that far from Equator...needs not be further than 2.0
                    hospitalLat.setError("That doesn't seem correct.");
                    return;
                }

                if (TextUtils.isEmpty(hospitalLng.getText()) || Double.parseDouble(hospitalLng.getText().toString()) < 34.0 || Double.parseDouble(hospitalLng.getText().toString()) > 37.0) {
                    hospitalLng.setError("That doesn't seem correct. Please check.");
                    return;
                }

                if (selectedKephLevel.equals("")) {
                    Toast.makeText(context, "Please select KEPH level.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (adminServicesAdapter.getSelectedServicesForHospital().isEmpty()) {
                    Toast.makeText(context, "Please indicate offered services.", Toast.LENGTH_LONG).show();
                    return;
                }

                //All validated, creating hospital object...
                newHospital = new hospitals();
                newHospital.setKephLevel(selectedKephLevel);
                newHospital.setName(hospitalNameEntry.getText().toString());
                newHospital.setLat(Double.parseDouble(hospitalLat.getText().toString()));
                newHospital.setLongi(Double.parseDouble(hospitalLng.getText().toString()));
                List<ServicesModel> offeredServicesList = new ArrayList<>();

                for (String ser : adminServicesAdapter.getSelectedServicesForHospital()) {
                    ServicesModel servicesModel = new ServicesModel();
                    servicesModel.setServiceName(ser);
                    offeredServicesList.add(servicesModel);
                }
                newHospital.setServicesOfferedList(offeredServicesList);
                saveHospitalToFireBase(newHospital, context);

                dialog.dismiss();
            }
        });

        Button btnDismiss = dialog.findViewById(R.id.btn_dismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void saveHospitalToFireBase(hospitals newInstanceOfHospital, final Context context) {

        //Loader progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Adding hospital...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        //Creating an instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Initialise a db if there's none
        DatabaseReference servicesDatabase = database.getReference("hospitals");
        //request a node for attachment
        String hId = servicesDatabase.push().getKey();
        //Starting the transaction service

        if (hId != null) {
            servicesDatabase.child(hId).setValue(newInstanceOfHospital);
            progressDialog.show();
        }

        servicesDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(context, "Hospital added.", Toast.LENGTH_LONG).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(context, "Saving Failed", Toast.LENGTH_LONG).show();
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void loadServicesList(final RecyclerView recyclerView, final TextView nothingLoadedServices, final Context context) {
        //Loading the list of services for user to select services offered by the new facility.
        database = FirebaseDatabase.getInstance();
        servicesDatabase = database.getReference("services");


        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        servicesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                servicesModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ServicesModel serviceModel = new ServicesModel();
                    serviceModel.setServiceId(dataSnapshot1.getKey());
                    Log.d("COLLECTED", dataSnapshot1.getValue().toString());
                    serviceModel.setServiceName(dataSnapshot1.getValue().toString());
                    servicesModelArrayList.add(serviceModel);
                }
                adminServicesAdapter = new AdminSelectServicesAdapter(context, servicesModelArrayList);
                recyclerView.setAdapter(adminServicesAdapter);
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
}
