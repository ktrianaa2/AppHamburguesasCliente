package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VerUbicacionActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mMap;
    private double latitud;
    private double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ubicacion);

        // Obtener datos enviados al iniciar la actividad
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int idUsuario = extras.getInt("idUsuario", 0);
            latitud = extras.getDouble("latitud", 0.0);
            longitud = extras.getDouble("longitud", 0.0);
            int tipoUbicacion = extras.getInt("tipoUbicacion", 0);

            // Imprimir la información en el Logcat
            Log.d("VerUbicacionActivity", "ID de usuario obtenida: " + idUsuario);
            Log.d("VerUbicacionActivity", "Tipo de Ubicacion obtenida: " + tipoUbicacion);
            Log.d("VerUbicacionActivity", "Latitud obtenida: " + latitud);
            Log.d("VerUbicacionActivity", "Longitud obtenida: " + longitud);
        }

        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);

        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y regresa a la anterior
                finish();
            }
        });

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ubicacion = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(ubicacion));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 25)); // Ajusta el zoom
        mMap.getUiSettings().setZoomControlsEnabled(false); // Desactiva los controles de zoom
        mMap.getUiSettings().setAllGesturesEnabled(false); // Desactiva todas las interacciones del usuario con el mapa
    }
}