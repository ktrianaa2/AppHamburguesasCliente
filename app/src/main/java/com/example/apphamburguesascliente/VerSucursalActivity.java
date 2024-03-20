package com.example.apphamburguesascliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apphamburguesascliente.Interfaces.DirectionsService;
import com.example.apphamburguesascliente.Modelos.DirectionsResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.maps.android.PolyUtil;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerSucursalActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private List<Marker> markerList = new ArrayList<>();

    //  sucursal
    private double latitudS;
    private double longitudS;
    private LatLng SUCURSAL_LOCATION;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private double currentLatitude; // Variable para almacenar la latitud actual
    private double currentLongitude; // Variable para almacenar la longitud actual
    private Marker currentMarker; // Marcador para la ubicación actual
    private Marker previousMarker; // Marcador para la ubicación anterior
    private Marker sucursalMarker; // Marcador de la sucursal
    private Polyline currentPolyline;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_sucursal);

        Intent intent = getIntent();
        if (intent != null) {
            int sucursalId = intent.getIntExtra("sucursal_id", -1);
            String sucursalNombre = intent.getStringExtra("sucursal_nombre");
            latitudS = intent.getDoubleExtra("latitud", 0.0);
            longitudS = intent.getDoubleExtra("longitud", 0.0);

            Log.d("VerSucursalActivity", "ID: " + sucursalId + ", Nombre: " + sucursalNombre +
                    ", Latitud: " + latitudS + ", Longitud: " + longitudS);

            TextView txtTitulo2 = findViewById(R.id.txtTitulo2);
            txtTitulo2.setText(sucursalNombre);

            SUCURSAL_LOCATION = new LatLng(latitudS, longitudS);

            // Agregar el marcador de la sucursal a la lista de marcadores al iniciar el mapa
            if (mMap != null && SUCURSAL_LOCATION != null) {
                sucursalMarker = mMap.addMarker(new MarkerOptions().position(SUCURSAL_LOCATION).title("Ubicación de la sucursal"));
                markerList.add(sucursalMarker);
            }


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }

        // permisos de la ubicacion para el mapa
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        MaterialButton comollegarBtn = findViewById(R.id.comollegarbtn);
        comollegarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verificar el número de marcadores en la lista
                int markerCount = markerList.size();

                if (markerCount == 1) {
                    // Si solo hay un marcador, indicar al usuario que agregue una ubicación de inicio de ruta
                    Log.d("VerSucursalActivity", "Marca una ubicación de inicio de la ruta");
                } else if (markerCount >= 2) {
                    // Si hay dos marcadores, establecer la ubicación de la sucursal como destino
                    LatLng sucursalLocation = null;
                    for (Marker marker : markerList) {
                        if (!marker.getTitle().equals("Ubicación de la sucursal")) {
                            sucursalLocation = SUCURSAL_LOCATION;
                            break;
                        }
                    }
                    if (sucursalLocation != null) {
                        //  trazar la ruta desde la nueva ubicación a la sucursal
                        LatLng currentLocation = currentMarker.getPosition();
                        obtenerRuta(currentLocation, sucursalLocation);
                        Log.d("VerSucursalActivity", "Trazar ruta desde " + currentMarker.getTitle() + " hacia la sucursal");
                    }
                }
            }
        });

        // Agregar el marcador de la sucursal a la lista de marcadores al iniciar el mapa
        if (mMap != null && SUCURSAL_LOCATION != null) {
            sucursalMarker = mMap.addMarker(new MarkerOptions().position(SUCURSAL_LOCATION).title("Ubicación de la sucursal"));
            markerList.add(sucursalMarker);
        }




        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);

        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y regresa a la anterior
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        if (intent != null) {
            double latitud = intent.getDoubleExtra("latitud", 0.0);
            double longitud = intent.getDoubleExtra("longitud", 0.0);

            // Mover la cámara a la ubicación recibida
            LatLng ubicacion = new LatLng(latitud, longitud);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));


            // Agregar el marcador de la sucursal a la lista de marcadores al iniciar el mapa
            if (SUCURSAL_LOCATION != null) {
                sucursalMarker = mMap.addMarker(new MarkerOptions().position(SUCURSAL_LOCATION).title("Ubicación de la sucursal"));
                markerList.add(sucursalMarker);
            }
        }

        mMap.setOnMapClickListener(this);

    }

    // Método para obtener y mostrar la ubicación actual
    private void showCurrentLocation() {
        if (mMap != null && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

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
                        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación Actual"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                        // Imprimir en el Logcat
                        Log.d("VerSucursalActivity", "Latitud (onMapReady): " + currentLatitude + ", Longitud (onMapReady): " + currentLongitude);
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
    public void onMapClick(LatLng latLng) {

        // Eliminar la ruta anterior si existe
        if (currentPolyline != null) {
            currentPolyline.remove();
        }

        // Eliminar la marca anterior si existe, pero solo si no es la ubicación de la sucursal
        if (currentMarker != null && !currentMarker.getTitle().equals("Ubicación de la sucursal")) {
            currentMarker.remove();
        }

        // Si hay una marca adicional (que no es la ubicación de la sucursal), elimínala
        if (previousMarker != null) {
            previousMarker.remove();
        }

        // Agregar una nueva marca en la ubicación clickeada
        currentLatitude = latLng.latitude;
        currentLongitude = latLng.longitude;

        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Nueva Ubicación"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Agregar el marcador a la lista
        if (!markerList.contains(currentMarker)) {
            markerList.add(currentMarker);
        }
        // Imprimir en el Logcat
        Log.d("VerSucursalActivity", "Latitud (onMapClick): " + currentLatitude + ", Longitud (onMapClick): " + currentLongitude);
    }

    private void obtenerRuta(LatLng origen, LatLng destino) {
        String baseUrl = "https://maps.googleapis.com/maps/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String rutaUrl = baseUrl + "directions/json?origin=" + origen.latitude + "," + origen.longitude + "&destination=" + destino.latitude + "," + destino.longitude + "&key=AIzaSyAn4V_mZEMc3VBnNHsfHSTEOvenJXYUz6Q";


        DirectionsService service = retrofit.create(DirectionsService.class);
        Call<DirectionsResponse> call = service.getDirections(
                origen.latitude + "," + origen.longitude,
                destino.latitude + "," + destino.longitude,
                "AIzaSyAn4V_mZEMc3VBnNHsfHSTEOvenJXYUz6Q"
        );

        // Eliminar la ruta anterior si existe
        if (currentPolyline != null) {
            currentPolyline.remove();
        }


        call.enqueue(new Callback<DirectionsResponse>() {

            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.isSuccessful()) {
                    DirectionsResponse directionsResponse = response.body();
                    dibujarRuta(directionsResponse);
                    Log.d("VerSucursalActivity", "Ruta obtenida correctamente: " + rutaUrl);
                } else {
                    Log.d("VerSucursalActivity", "Error al obtener la ruta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Log.e("VerSucursalActivity", "Error en la solicitud: " + t.getMessage());
            }
        });
    }

    private void dibujarRuta(DirectionsResponse response) {
        // Verificar si la respuesta contiene rutas
        if (response != null && response.getRoutes() != null && response.getRoutes().size() > 0) {
            // Obtener la polilínea de la primera ruta
            String polyline = response.getRoutes().get(0).getOverviewPolyline().getPoints();

            // Decodificar la polilínea en una lista de puntos LatLng
            List<LatLng> puntos = PolyUtil.decode(polyline);

            // Crear un objeto PolylineOptions para configurar las propiedades de la polilínea
            PolylineOptions opcionesLinea = new PolylineOptions()
                    .addAll(puntos)
                    .width(10)
                    .color(Color.BLUE); // Puedes cambiar el color si lo deseas

            // Agregar la polilínea al mapa
            currentPolyline = mMap.addPolyline(opcionesLinea);
        }
    }

}
