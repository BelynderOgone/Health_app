package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.admin.HospitalFragment;
import com.example.myapplication.admin.ServicesFragment;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_facilities_list) {
            Toast.makeText(this, "Launch facilities list for user.", Toast.LENGTH_LONG).show();

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
        map.moveCamera(CameraUpdateFactory .newLatLng(Kisumu));
        Polygon polygon = map.addPolygon(kWestMap);
        map.setMinZoomPreference(10);
        map.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(-0.044, 34.628), new LatLng(-0.044, 34.628)));



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

