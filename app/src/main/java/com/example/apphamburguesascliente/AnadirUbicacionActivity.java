package com.example.apphamburguesascliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;

import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class AnadirUbicacionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Marker currentMarker;
    private double currentLatitude; // Variable para almacenar la latitud actual
    private double currentLongitude; // Variable para almacenar la longitud actual


    private boolean mapReady = false;
    GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_ubicacion);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);

        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y regresa a la anterior
                finish();
            }
        });

        // permisos de la ubicacion para el mapa
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Si ya se tienen los permisos, obtén y muestra la ubicación actual
            showCurrentLocation();
        }

        MaterialButton ubicacionActualBtn = findViewById(R.id.ubiactualbtn);
        ubicacionActualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Al hacer clic en el botón de ubicación actual, actualiza la ubicación
                showCurrentLocation();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int idUsuario = extras.getInt("idUsuario", 0);

            // Imprimir la información en el Logcat
            Log.d("EditarUbicacionActivity", "ID de usuario obtenida: " + idUsuario);
        }

    }

    // Método para obtener y mostrar la ubicación actual
    private void showCurrentLocation() {
        if (mapReady && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();

                        LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));

                        // Imprimir en el Logcat
                        Log.d("AnadirUbicacionActivity", "Latitud (onMapReady): " + currentLatitude + ", Longitud (onMapReady): " + currentLongitude);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si se otorgan los permisos, obtén y muestra la ubicación actual
                showCurrentLocation();
            }
        }
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        updateMarker(latLng);
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        updateMarker(latLng);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        // El mapa está listo, establecer mapReady a true
        mapReady = true;

        // Centrar el mapa en Quevedo sin marcar ningún punto
        LatLng quevedoLatLng = new LatLng(-1.02863, -79.46352);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(quevedoLatLng, 15f));
    }

    private void updateMarker(LatLng latLng) {
        mMap.clear(); // Limpiar marcadores existentes

        // Crear un nuevo marcador en la ubicación dada
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Actualizar variables de latitud y longitud
        currentLatitude = latLng.latitude;
        currentLongitude = latLng.longitude;

        // Imprimir en el Logcat
        Log.d("AnadirUbicacionActivity", "Latitud: " + currentLatitude + ", Longitud: " + currentLongitude);
    }
}