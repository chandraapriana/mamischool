package com.chandra.mamischool.ActivityStudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import com.chandra.mamischool.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StudentZooning extends AppCompatActivity  implements OnMapReadyCallback{
    GoogleMap map;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_zooning);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapZooning);

        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(StudentZooning.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mapFragment.getMapAsync( this);
        }else{
            ActivityCompat.requestPermissions(StudentZooning.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==44){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                mapFragment.getMapAsync( this);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location!=null){
                    LatLng user = new LatLng(location.getLatitude(),location.getLongitude());
                    Float latitude = Float.parseFloat(getIntent().getStringExtra("latitude"));
                    Float longitude = Float.parseFloat(getIntent().getStringExtra("longitude"));
                    String radius = getIntent().getStringExtra("radius");

                    System.out.println("========================================="+getIntent().getStringExtra("schoolName"));
                    LatLng school = new LatLng(latitude, longitude);
                    map.addMarker(new MarkerOptions().position(school).title(getIntent().getStringExtra("schoolName")));
                    map.addCircle(new CircleOptions().center(school).radius(Integer.parseInt(radius)).strokeWidth(3f).strokeColor(Color.RED).fillColor(Color.argb(75,191,191,191)));
                    map.addMarker(new MarkerOptions().position(user).title("You Here...").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_student_32)));

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(school,14f));

                }
            }
        });
    }

    //    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//
//        Float latitude = Float.parseFloat(getIntent().getStringExtra("latitude"));
//        Float longitude = Float.parseFloat(getIntent().getStringExtra("longitude"));
//
//
//        System.out.println("========================================="+getIntent().getStringExtra("schoolName"));
//        LatLng school = new LatLng(latitude, longitude);
//        map.addMarker(new MarkerOptions().position(school).title(getIntent().getStringExtra("schoolName")));
//        map.addCircle(new CircleOptions().center(school).radius(3000).strokeWidth(3f).strokeColor(Color.RED).fillColor(Color.argb(75,191,191,191)));
//
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(school,14f));
//    }
}
