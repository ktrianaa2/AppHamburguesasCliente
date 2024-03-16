package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.Sucursal;
import com.example.apphamburguesascliente.Modelos.SucursalResponse;
import com.example.apphamburguesascliente.Modelos.Ubicacion;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerSucursalActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ApiService apiService;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_sucursal);

        // Obtener los datos de la sucursal de los extras del Intent
        int sucursalId = getIntent().getIntExtra("sucursal_id", -1);
        String sucursalNombre = getIntent().getStringExtra("sucursal_nombre");

        // Mostrar los datos en el LogCat
        Log.d("VerSucursalActivity", "ID de la sucursal: " + sucursalId);
        Log.d("VerSucursalActivity", "Nombre de la sucursal: " + sucursalNombre);

        // Iniciar el servicio API
        apiService = ApiClient.getInstance();

        // Obtener y mostrar la ubicación de la sucursal
        obtenerUbicacionSucursal(sucursalId);
    }

    // Método para obtener la ubicación de la sucursal
    private void obtenerUbicacionSucursal(int idSucursal) {
        apiService.obtenerSucursales().enqueue(new Callback<SucursalResponse>() {
            @Override
            public void onResponse(Call<SucursalResponse> call, Response<SucursalResponse> response) {
                if (response.isSuccessful()) {
                    SucursalResponse sucursalResponse = response.body();
                    if (sucursalResponse != null && sucursalResponse.getSucursalList() != null) {
                        for (Sucursal sucursal : sucursalResponse.getSucursalList()) {
                            if (sucursal.getIdSucursal() == idSucursal) {
                                int idUbicacion = 8;
                                obtenerLatLongUbicacion(idUbicacion);
                                return;
                            }
                        }
                        Log.e("VerSucursalActivity", "No se encontró la sucursal con ID: " + idSucursal);
                    } else {
                        Log.e("VerSucursalActivity", "La respuesta de la API es nula o no contiene sucursales");
                    }
                } else {
                    Log.e("VerSucursalActivity", "Error al obtener las sucursales: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SucursalResponse> call, Throwable t) {
                Log.e("VerSucursalActivity", "Error al obtener las sucursales: " + t.getMessage(), t);
            }
        });
    }

    // Método para obtener la latitud y longitud de la ubicación
    private void obtenerLatLongUbicacion(int idUbicacion) {
        String latitud = "latitud_obtenida";
        String longitud = "longitud_obtenida";

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(latitud);
        ubicacion.setLongitud(longitud);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
