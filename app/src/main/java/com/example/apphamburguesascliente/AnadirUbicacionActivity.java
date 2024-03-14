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

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.Ubicacion;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;
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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnadirUbicacionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {


    private ApiService apiService;
    private User currentUser;
    private int idUsuario;
    private int tipoUbicacion;
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


        apiService = ApiClient.getInstance();


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
            idUsuario = extras.getInt("idUsuario", 0);
          //  currentUser = obtenerUsuarioPorId(idUsuario);
            tipoUbicacion = extras.getInt("tipoUbicacion", 0);

            // Imprimir la información en el Logcat para verificar
            Log.d("AnadirUbicacionActivity", "ID de cliente obtenido: " + idUsuario);
            Log.d("AnadirUbicacionActivity", "Tipo de ubicación obtenido: " + tipoUbicacion);
        }

        MaterialButton guardarBtn = findViewById(R.id.guardarbtn);
        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarUbicacion();
            }
        });

    }



    private void guardarUbicacion() {
        if (currentLatitude != 0 && currentLongitude != 0) {
            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setLatitud(String.valueOf(currentLatitude));
            ubicacion.setLongitud(String.valueOf(currentLongitude));

            // Agregar una línea de registro en el Logcat
            Log.d("AnadirUbicacionActivity", "Guardando ubicación: Latitud " + currentLatitude + ", Longitud " + currentLongitude);

            // Obtener el usuario correspondiente
            obtenerUsuarioPorId(idUsuario, ubicacion);
        } else {
            Toast.makeText(this, "Seleccione una ubicación primero", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerUsuarioPorId(int idUsuario, final Ubicacion ubicacion) {

        Call<UserResponse> callUsuario = apiService.obtenerUsuario(String.valueOf(idUsuario));


        callUsuario.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        User currentUser = userResponse.getUsuario();
                        // Actualizar el usuario con la ubicación y guardar
                        guardarUbicacionConUsuario(currentUser, ubicacion);
                    }
                } else {
                    // Manejar errores de respuesta
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Manejar errores de red
            }
        });
    }

    private void guardarUbicacionConUsuario(User currentUser, Ubicacion ubicacion) {
        switch (tipoUbicacion) {
            case 1:
                currentUser.setUbicacion1(ubicacion);
                break;
            case 2:
                currentUser.setUbicacion2(ubicacion);
                break;
            case 3:
                currentUser.setUbicacion3(ubicacion);
                break;
            default:
                break;
        }

        // Actualizar el usuario en el servidor
        actualizarUsuario(String.valueOf(idUsuario), currentUser);
    }

    private void actualizarUsuario(String idCuenta, User user) {
        Call<UserResponse> call = apiService.actualizarUsuario(idCuenta, user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    // Usuario actualizado exitosamente
                    Log.d("AnadirUbicacionActivity", "Usuario actualizado exitosamente");
                    finish(); // Cerrar la actividad actual
                } else {
                    // Manejar errores de respuesta
                    Log.e("AnadirUbicacionActivity", "Error al actualizar el usuario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Manejar errores de red o del servidor
                Log.e("AnadirUbicacionActivity", "Error de red o del servidor al actualizar el usuario: " + t.getMessage());
            }
        });
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