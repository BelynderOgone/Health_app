package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.SelectFacilityDialogAdapter;
import com.example.myapplication.auth.LoginActivity;
import com.example.myapplication.models.ServicesModel;
import com.example.myapplication.models.hospitals;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    FirebaseDatabase database;
    DatabaseReference hospitalDatabase;
    ArrayList<String> dataArr;
    ArrayList<hospitals> matchingSelection;
    ArrayAdapter<String> newsAdapter;
    androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete;


    RecyclerView selectedMatchRecycler;
    private ArrayList<hospitals> hospitalsArrayList;
    SelectFacilityDialogAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();

        hospitalDatabase = database.getReference("hospitals");
        hospitalsArrayList = new ArrayList<>();
        dataArr = new ArrayList<String>();
        matchingSelection = new ArrayList<>();

        //In the manifest, mark the Main activity as launcher, but check if there is a
        //signed in User before continuing.

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            //If there is no user signed in, start the Login Activity, else continue
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish(); //Kill this activity to avoid back press
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        loadData();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        //Inialize the search item/Widget
        final MenuItem menuItem = menu.findItem(R.id.action_facilities_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search by service...");
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        //Attaching a textview for autocompletion/suggestion
        searchAutoComplete = (androidx.appcompat.widget.SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        searchAutoComplete.setDropDownBackgroundResource(android.R.color.background_light);


        //Feed the suggestions from services offerd
        newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);

        //Add a listener for selction from suggestions
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Displaying a popup with facilities matching selection
                matchingSelection.clear();

                //Back-filter facilities that match selection

                for (hospitals facility : hospitalsArrayList) {
                    for (ServicesModel services_ : facility.getServicesOfferedList()) {
                        if (services_.getServiceName().equals(newsAdapter.getItem(position))) {
                            matchingSelection.add(facility);

                        }
                    }
                }

                final Dialog dialog = new Dialog(MainActivity.this);

                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_select_facility);
                Button btndialog = (Button) dialog.findViewById(R.id.btn_dismiss_dialog);
                btndialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
                dialog.show();
                selectedMatchRecycler = dialog.findViewById(R.id.recycler_facilities_dialog);
                selectedMatchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                arrayAdapter = new SelectFacilityDialogAdapter(getApplicationContext(), matchingSelection);
                selectedMatchRecycler.setAdapter(arrayAdapter);


                //dialog.show();
                arrayAdapter.setOnBluetoothDeviceClickedListener(new SelectFacilityDialogAdapter.OnBluetoothDeviceClickedListener() {
                    @Override
                    public void onBluetoothDeviceClicked(hospitals selectedHospital) {
                        dialog.dismiss();
                        if (map != null) {
                            LatLng selected = new LatLng(selectedHospital.getLat(), selectedHospital.getLongi());
                            map.moveCamera(CameraUpdateFactory.newLatLng(selected));
                            map.setBuildingsEnabled(true);

                            map.addMarker(new MarkerOptions().position(selected).title(selectedHospital.getName()));

                            map.animateCamera(CameraUpdateFactory.zoomTo(17));
                        }else{
                            Toast.makeText(getApplicationContext(), "Map not initialized", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });


        return true;
    }

    public void loadData() {
        hospitalDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Clear the previous list before refilling
                hospitalsArrayList.clear();
                dataArr.clear();
                //Loop through the data and add to list
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //create an object of type hospital
                    hospitals hospitalModel = new hospitals();
                    //Set the node value as key
                    hospitalModel.sethId(dataSnapshot1.getKey());
                    //Assigning the name of hospital
                    hospitalModel.setName(dataSnapshot1.child("name").getValue().toString());
                    //pull its services by loading
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
                if (hospitalsArrayList.size() > 0) {
                    Log.d("HOSPITAL COUNT", String.valueOf(hospitalsArrayList.size()));
                    for (hospitals single : hospitalsArrayList) {

                        for (ServicesModel services : single.getServicesOfferedList()) {

                            if (!dataArr.contains(services.getServiceName())) {
                                dataArr.add(services.getServiceName());

                            }

                        }
                    }
                    //Inject the list to the adapter once there's data
                    newsAdapter.notifyDataSetChanged();
                    searchAutoComplete.setAdapter(newsAdapter);


                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DATABASE ERROR", databaseError.getMessage());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_facilities_search) {
            //When services have not been loaded yet -for network reasons or whatever, notify the user.
            if (dataArr.size() < 1) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Still sorting services from facilities. . .");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Wait",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

        } else if (item.getItemId() == R.id.action_log_out) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();//terminate session---no going back to main activity
        } else if (item.getItemId() == R.id.action_help) {
            Toast.makeText(this, "Launch assistant page.", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        LatLng Kisumu = new LatLng(-0.05, 34.619);

        //This draws the polygon, it can optionally be removed but leave the focus to Kisumu West.
        PolygonOptions kWestMap = new PolygonOptions().add(
                new LatLng(0.009, 34.604),
                new LatLng(-0.058, 34.568),
                new LatLng(-0.141, 34.605),
                new LatLng(-0.094, 34.685),
                new LatLng(-0.006, 34.653)
        );

        //map.addMarker(new MarkerOptions().position(Kisumu).title("Kisumu"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Kisumu));
        Polygon polygon = map.addPolygon(kWestMap);
        map.setMinZoomPreference(10);
        //The following line can be uncommented to restrict the area a map can show
        //map.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(-0.5, 35.0), new LatLng(-0.5, 35.0)));


        // map.addPolygon(polygon).setStrokeColor(Color.GREEN);
        map.animateCamera(CameraUpdateFactory.zoomTo(12));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mLastLocation = location;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    map.addMarker(new MarkerOptions().position(latLng).title("Your current location"));
                    map.animateCamera(CameraUpdateFactory.zoomTo(11));


                }
            }
        }
    };

}

