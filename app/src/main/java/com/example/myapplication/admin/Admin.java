package com.example.myapplication.admin;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.adapters.TabbedAdminPageAdapter;
import com.example.myapplication.models.ServicesModel;
import com.example.myapplication.models.hospitals;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin extends AppCompatActivity implements  HospitalFragment.OnListFragmentInteractionListener, ServicesFragment.OnListFragmentInteractionListener {
    EditText name;
    EditText latitude;
    EditText longitude;
    EditText kephLevel;
    Button save;

    TabLayout adminTabLayout;
    ViewPager adminTabsViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if is logged in as admin

        setContentView(R.layout.activity_admin);

        adminTabLayout = findViewById(R.id.tablayout_admin);
        adminTabsViewPager = findViewById(R.id.administrator_viewpager);
        //initialise viewpager

        adminTabLayout.setupWithViewPager(adminTabsViewPager);
        //assign tabslayout a viewpager

        TabbedAdminPageAdapter tabsAdapter = new TabbedAdminPageAdapter(getSupportFragmentManager());
        //Add tabs for service & facility
        tabsAdapter.addFragment(new HospitalFragment(), "Manage Hospitals");
        tabsAdapter.addFragment(new ServicesFragment(), "Offered services");
        //Assign the loaded adapter
        adminTabsViewPager.setAdapter(tabsAdapter);


        //save = findViewById(R.id.btn_save);

        /*save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHospital();
            }
        });
    }

   /* private void saveHospital() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference hospitaldb = database.getReference("hospital");
        name = findViewById(R.id.hsp_name);
        latitude = findViewById(R.id.hsp_lat);
        longitude = findViewById(R.id.hsp_logi);

        kephLevel = findViewById(R.id.other_services);

        String Hid = hospitaldb.push().getKey();
        String h_name = name.getText().toString().trim();
        String h_lat = latitude.getText().toString().trim();
        String h_long = longitude.getText().toString().trim();
        String hospitalKephLevel = kephLevel.getText().toString().trim();

        hospitals hospitals = new hospitals(Hid, h_name, h_lat, h_long,"", hospitalKephLevel);

        hospitaldb.setValue(h_name);
    }*/
}

    @Override
    public void onListFragmentInteraction(hospitals item) {

    }

    @Override
    public void onListFragmentInteraction(ServicesModel item) {

    }
}