package com.example.myapplication.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
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
        setContentView(R.layout.activity_add_hospital);


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



        String h_name = name.getText().toString().trim();
        String h_lat = latitude.getText().toString().trim();
        String h_long = longitude.getText().toString().trim();


        String hId = hospitaldb.push().getKey();

        hospitals hospitals = new hospitals(hId, h_name, h_lat, h_long, "", "");

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
