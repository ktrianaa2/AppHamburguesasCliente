package com.example.apphamburguesascliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.Ubicacion;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarUbicacionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private ApiService apiService;
    private int idUsuario;
    private int tipoUbicacion;

    private GoogleMap mMap;
    private Marker currentMarker;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ubicacion);

        // Inicializar ApiService
        apiService = ApiClient.getInstance();

        // Obtener datos enviados al iniciar la actividad
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuario = extras.getInt("idUsuario", 0);
            double latitud = extras.getDouble("latitud", 0.0);
            double longitud = extras.getDouble("longitud", 0.0);
            tipoUbicacion = extras.getInt("tipoUbicacion", 0);

            // Imprimir la información en el Logcat
            Log.d("EditarUbicacionActivity", "ID de usuario obtenida: " + idUsuario);
            Log.d("EditarUbicacionActivity", "Tipo de Ubicacion obtenida: " + tipoUbicacion);
            Log.d("EditarUbicacionActivity", "Latitud obtenida: " + latitud);
            Log.d("EditarUbicacionActivity", "Longitud obtenida: " + longitud);

            // Inicializar latitud y longitud actuales
            currentLatitude = latitud;
            currentLongitude = longitud;
        }

        // Configurar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Configurar botón de cancelar
        MaterialButton cancelarBtn = findViewById(R.id.cancelarbtn);
        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si la ubicación actual coincide con la ubicación recibida
                if (currentLatitude == getIntent().getDoubleExtra("latitud", 0.0) &&
                        currentLongitude == getIntent().getDoubleExtra("longitud", 0.0)) {
                    // Si no hay cambios, cerrar la actividad
                    finish();
                } else {
                    // Si hay cambios, restaurar la ubicación original
                    setOriginalLocation();
                }
            }
        });

        // Configurar botón de ubicación actual
        MaterialButton ubicacionActualBtn = findViewById(R.id.ubiactualbtn);
        ubicacionActualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
        });

        // Configurar botón de guardar
        MaterialButton guardarBtn = findViewById(R.id.guardarbtn);
        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarUbicacion();
            }
        });

        // Configurar flecha de retroceso
        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);
        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        // Mostrar la ubicación recibida
        double latitud = getIntent().getDoubleExtra("latitud", 0.0);
        double longitud = getIntent().getDoubleExtra("longitud", 0.0);
        LatLng location = new LatLng(latitud, longitud);
        currentLatitude = latitud;
        currentLongitude = longitud;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
        currentMarker = mMap.addMarker(new MarkerOptions().position(location));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // Actualizar el marcador con la ubicación seleccionada por el usuario
        currentLatitude = latLng.latitude;
        currentLongitude = latLng.longitude;
        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng));

        // Imprimir las nuevas coordenadas en el Logcat
        Log.d("EditarUbicacionActivity", "Nueva ubicación - Latitud: " + currentLatitude + ", Longitud: " + currentLongitude);
    }

    private void showCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                        if (currentMarker != null) {
                            currentMarker.remove();
                        }
                        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                    } else {
                        Toast.makeText(EditarUbicacionActivity.this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void guardarUbicacion() {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(String.valueOf(currentLatitude));
        ubicacion.setLongitud(String.valueOf(currentLongitude));

        // Log para verificar la nueva ubicación
        Log.d("EditarUbicacionActivity", "Nueva ubicación guardada - Latitud: " + currentLatitude + ", Longitud: " + currentLongitude);

        // Actualizar la ubicación en el servidor
        switch (tipoUbicacion) {
            case 1:
                actualizarUsuario(idUsuario, ubicacion, null, null);
                break;
            case 2:
                actualizarUsuario(idUsuario, null, ubicacion, null);
                break;
            case 3:
                actualizarUsuario(idUsuario, null, null, ubicacion);
                break;
            default:
                // Manejar caso inválido
                break;
        }
    }

    private void actualizarUsuario(int idUsuario, Ubicacion ubicacion1, Ubicacion ubicacion2, Ubicacion ubicacion3) {
        // Realizar la llamada a la API para actualizar el usuario
        Call<UserResponse> call = apiService.actualizarUsuario(
                String.valueOf(idUsuario),
                ubicacion1 != null ? ubicacion1.getLatitud() : "",
                ubicacion1 != null ? ubicacion1.getLongitud() : "",
                ubicacion2 != null ? ubicacion2.getLatitud() : "",
                ubicacion2 != null ? ubicacion2.getLongitud() : "",
                ubicacion3 != null ? ubicacion3.getLatitud() : "",
                ubicacion3 != null ? ubicacion3.getLongitud() : ""
        );
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    // Imprimir mensaje de éxito en el Logcat
                    Log.d("EditarUbicacionActivity", "Ubicación guardada exitosamente");
                    // Cerrar la actividad
                    finish();
                } else {
                    // Imprimir mensaje de error en el Logcat
                    Log.e("EditarUbicacionActivity", "Error al guardar la ubicación");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Imprimir mensaje de error en el Logcat
                Log.e("EditarUbicacionActivity", "Error de conexión al guardar la ubicación");
            }
        });
    }

    private void setOriginalLocation() {
        double latitud = getIntent().getDoubleExtra("latitud", 0.0);
        double longitud = getIntent().getDoubleExtra("longitud", 0.0);
        LatLng location = new LatLng(latitud, longitud);
        currentLatitude = latitud;
        currentLongitude = longitud;
        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
    }


}
