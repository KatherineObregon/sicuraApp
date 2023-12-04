package com.example.sicuraapp.Entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.sicuraapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Marcadores {
    GoogleMap nMap;
    Context context;


    public Marcadores(GoogleMap nMap, Context context){
        this.nMap=nMap;
        this.context= context;
    }

    public void addMarkersDefault(){
        uno(-12.198191,-77.013433, "uno");
        uno(-12.204041,-77.013130, "dos");
        uno(-12.176885,-77.005951, "tres");
        uno(-12.100800,-76.971918, "cuatro");
        uno(-12.072289,-76.955109, "cinco");
        uno(-12.991089,-77.060859, "seis");



    }

    public void uno (Double latitud, Double longitud, String titulo){
        LatLng punto = new LatLng(latitud, longitud);
        int height=140;
        int width=165;
        BitmapDrawable uno= (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_marcador);
        Bitmap unos= uno.getBitmap();
        Bitmap uns= Bitmap.createScaledBitmap(unos, width, height, false);
        nMap.addMarker(new MarkerOptions().position(punto).title(titulo).snippet("uno").
                icon(BitmapDescriptorFactory.fromBitmap(uns)));


    }

}
