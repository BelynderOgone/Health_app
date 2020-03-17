package com.example.myapplication.admin;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddServiceDialog {
    private EditText serviceName;

    public void showDialog(Activity activity, final Context context) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //User is not allowed to dismiss the entry just simply
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_add_services);
        //Initialise the field for service name
        serviceName = dialog.findViewById(R.id.service_name);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_save);
        //Add action listener to saving service
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (serviceName.getText().length() < 5) {
                    //If the service name is less that 5 characters, thats not satisfactory - set error
                    serviceName.setError("Name be at least 5 characters.");
                } else {
                    //If length of content is OK, Proceed to upload
                    dialog.dismiss();
                    saveServiceToFireBase(serviceName.getText().toString(), context);

                }
            }
        });

        Button btnDismiss = dialog.findViewById(R.id.btn_dismiss);
        //For dismissing the addition listener
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void saveServiceToFireBase(String serviceName, final Context context) {

        //Loader progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Saving service...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        //Creating an instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Initialise a db if there's none
        DatabaseReference servicesDatabase = database.getReference("services");
        //request a node for attachment
        String hId = servicesDatabase.push().getKey();
        //Starting the transaction service

        if (hId != null) {
            servicesDatabase.child(hId).setValue(serviceName);
            progressDialog.show();
        }

        servicesDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(context, "Service added.", Toast.LENGTH_LONG).show();
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
}
