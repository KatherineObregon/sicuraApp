package com.example.sicuraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class CompartirRuta2 extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    public static int LOCATION_REQUEST_CODE = 100;
    FusedLocationProviderClient fusedLocationProviderClient;

    double latActual;
    double longActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartir_ruta2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa_compartir2);
        if(mapFragment!=null){
            mapFragment.getMapAsync(this);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();

    }

    private void checkLocationPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getUserLocation();
        }else {
            requestForPermissions();
        }
    }

    private void getUserLocation() {

        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    latActual= location.getLatitude();
                    longActual = location.getLongitude();
                    Log.d("msg", latActual +","+longActual);




                }
            }
        });
    }

    private void requestForPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permiso aceptado", Toast.LENGTH_SHORT).show();
                getUserLocation();
            }else{
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
            
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map= googleMap;
//        LatLng userLocation = new LatLng(latActual, longActual);
//
//        map.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//        map.animateCamera(CameraUpdateFactory.zoomTo(12));
//
//        map.addMarker(new MarkerOptions().position(userLocation).title("Posición actual real"));

        double latitudPUCP =-12.0690;
        double longitudPUCP=-77.0781;

        LatLng latLng = new LatLng(latitudPUCP,longitudPUCP);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.moveCamera(CameraUpdateFactory.zoomTo(12f));
        map.animateCamera(CameraUpdateFactory.zoomTo(12f));

        map.addMarker(new MarkerOptions().position(latLng).title("Posición actual"));



    }
}