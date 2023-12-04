package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.sicuraapp.Entities.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.type.Color;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrasarLinea_compartirRuta extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap nMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasar_linea_compartir_ruta);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa_compartir_trasado);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        nMap.setMyLocationEnabled(true);
        nMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        ArrayList<LatLng> points;  // No es necesario inicializarlo a null aquí
        PolylineOptions lineOptions = null;

        for (int i = 0; i < Utils.routes.size(); i++) {
            Log.d("aqui", String.valueOf(Utils.routes.size()));

            // No es necesario reinicializar points en cada iteración
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = Utils.routes.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(9);
            // Aquí falta algo, por ejemplo, establecer el color de la polilínea
            lineOptions.color(Color.BLUE_FIELD_NUMBER);

            // Aquí podrías añadir la polilínea al mapa, si lo deseas
            // googleMap.addPolyline(lineOptions);
        }

        }
}
