package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.models.hospitals;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Reg extends AppCompatActivity {

    EditText name;
    EditText latitude;
    EditText longitude;
    EditText main_service;
    EditText main_srv1;
    EditText main_srv2;
    EditText main_serv3;
    EditText other_srvc;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);


        save = findViewById(R.id.btn_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    private void Register() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference hospitaldb = database.getReference("hospital");

        name = findViewById(R.id.hsp_name);
        latitude = findViewById(R.id.hsp_lat);
        longitude = findViewById(R.id.hsp_logi);
        main_service = findViewById(R.id.main_service);
        main_srv1 = findViewById(R.id.main_s1);
        main_srv2 = findViewById(R.id.mains2);
        main_serv3 = findViewById(R.id.main_s3);
        other_srvc = findViewById(R.id.other_services);


        String h_name = name.getText().toString().trim();
        String h_lat = latitude.getText().toString().trim();
        String h_long = longitude.getText().toString().trim();
        String h_service = main_service.getText().toString().trim();
        String h_service1 = main_srv1.getText().toString().trim();
        String h_service2 = main_srv2.getText().toString().trim();
        String h_service3 = main_serv3.getText().toString().trim();
        String other_services = other_srvc.getText().toString().trim();

        String hId = hospitaldb.push().getKey();

        hospitals hospitals = new hospitals(hId, h_name, h_lat, h_long, h_service, h_service1, h_service2, h_service3, other_services);

        hospitaldb.child(hId).setValue(hospitals);
        hospitaldb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(Reg.this, "record saved",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Reg.this, "Saving Failed", Toast.LENGTH_LONG).show();
                Toast.makeText(Reg.this, databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
