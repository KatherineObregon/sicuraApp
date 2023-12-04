package com.example.sicuraapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sicuraapp.Entities.GPSController;
import com.example.sicuraapp.Entities.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompartirRuta extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener , GoogleMap.OnMarkerClickListener{

    Context context;
    GoogleMap nMap;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    Location location;
    GPSController gpsTracker;
    SensorManager sensorManager;
    double latitudActual;
    double longitudActual;
    double latitudInicio =-12.0690;
    double longitudInicio=-77.0781;


    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener((SensorEventListener) this);
    }
    public void obtenerUbicacion(View view){
        int permissionCoarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCoarse== PackageManager.PERMISSION_GRANTED && permissionFine==PackageManager.PERMISSION_GRANTED){
            //tengo permisos
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        latitudActual = location.getLatitude();
                        longitudActual = location.getLongitude();
                    }
                }
            });

        }else {
            //no tengo permisos
            requestPermissionLocation.launch(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
        }

    }
    ActivityResultLauncher<String[]> requestPermissionLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartir_ruta);

        requestPermissionLocation = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                Boolean clGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                Boolean flGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);

                if(flGranted !=null &&flGranted){
                    Log.d("msg", "me dio permisos fine");
                    obtenerUbicacion(null);
                }else if(clGranted!= null && clGranted){
                    Log.d("msg", "me dio permisos coarsed");

                }else{
                    Log.d("msg","no me dio permisos");
                }

            }
        });

        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        gpsTracker = new GPSController(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa_compartir);
        mapFragment.getMapAsync(this);

        request = Volley.newRequestQueue(getApplicationContext());


    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Utils.coordenadas.setOrigenLat(latLng.latitude);
        Utils.coordenadas.setDestinoLat(latLng.longitude);

        Toast.makeText(context, "Toque el ícono para seleccionar", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        nMap = googleMap;
        LatLng posicion = new LatLng(latitudInicio, longitudInicio);
        nMap.addMarker(new MarkerOptions()
                .position(posicion)
                .title("Posicion actual"));

        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion,17));
        Utils.markersDefault(nMap, getApplicationContext());
        nMap.setOnMapClickListener(this);
        nMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);



    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                Toast.makeText(this, "You didn't give permission to access device location", Toast.LENGTH_LONG).show();
                startInstalledAppDetailsActivity();
            }
        }
    }
    public void ObtenerRuta(String latInicial, String lngInicial, String latFinal, String lngFinal){

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latInicial + "," + lngInicial + "&destination=" + latFinal + "," + lngFinal + "&key=AIzaSyBjys4VTBIQyVQiRROpstOySZmGfMrUe6Y&mode=drive";



        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jRoutes = null;
                JSONArray jLegs = null;
                JSONArray jSteps = null;


                try {

                    jRoutes = response.getJSONArray("routes");


                    for(int i=0;i<jRoutes.length();i++){

                        jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                        List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                        for(int j=0;j<jLegs.length();j++){
                            jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");


                            for(int k=0;k<jSteps.length();k++){

                                String polyline = "";
                                polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                                List<LatLng> list = decodePoly(polyline);

                                for(int l=0;l<list.size();l++){

                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                                    hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                                    path.add(hm);

                                }
                            }

                            Utils.routes.add(path);

                            Intent intent = new Intent(CompartirRuta.this, TrasarLinea_compartirRuta.class);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                Log.d("ERROR: ", error.toString());
            }
        }
        );

        request.add(jsonObjectRequest);

    }


    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }







    public  class MyAsyncTask extends AsyncTask<Integer, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();




        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {
                while (location == null){
                    location = gpsTracker.getLocation();
                    publishProgress(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            location = gpsTracker.getLocation();
            publishProgress(2);

            return "Fin";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if(values[0] == 0){
                Log.d("Asyntask", "null");
            }else{
                Log.d("Asyntask", "Coordenadas");
                Toast.makeText(CompartirRuta.this, "LISTO", Toast.LENGTH_SHORT).show();
                Utils.coordenadas.setOrigenLat(location.getLatitude());
                Utils.coordenadas.setOrigenLng(location.getLongitude());
                Log.d("Asyntask", String.valueOf(location.getLatitude()));
                Log.d("Asyntask", String.valueOf(location.getLongitude()));
                ObtenerRuta(String.valueOf(Utils.coordenadas.getOrigenLat()), String.valueOf(Utils.coordenadas.getOrigenLng()),
                        String.valueOf(Utils.coordenadas.getDestinoLat()), String.valueOf(Utils.coordenadas.getDestinoLong()));

            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("asyntask", "FIN");

        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        Utils.coordenadas.setDestinoLat(marker.getPosition().latitude);
        Utils.coordenadas.setDestinoLong(marker.getPosition().longitude);
        return false;
    }
    private Boolean permissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == ((PackageManager.PERMISSION_GRANTED));
    }
    private void startInstalledAppDetailsActivity() {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}