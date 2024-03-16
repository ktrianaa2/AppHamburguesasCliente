package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.apphamburguesascliente.Adaptadores.SucursalAdaptador;
import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.Sucursal;
import com.example.apphamburguesascliente.Modelos.SucursalResponse;
import com.example.apphamburguesascliente.Modelos.Ubicacion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SucursalesActivity extends AppCompatActivity implements SucursalAdaptador.OnSucursalClickListener {

    private ApiService apiService;
    private SucursalAdaptador adapter;
    private List<Sucursal> sucursalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursales);

        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);

        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y regresa a la anterior
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.sucursalesRecycler);
        sucursalList = new ArrayList<>();
        adapter = new SucursalAdaptador(sucursalList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        obtenerDatosDeAPI();
    }

    private void obtenerDatosDeAPI() {

        apiService = ApiClient.getInstance();

        // Realizar la llamada a la API
        Call<SucursalResponse> call = apiService.obtenerSucursales();

        // Ejecutar la llamada de manera asíncrona
        call.enqueue(new Callback<SucursalResponse>() {
            @Override
            public void onResponse(Call<SucursalResponse> call, Response<SucursalResponse> response) {
                if (response.isSuccessful()) {
                    // Si la solicitud es exitosa, obtén la lista de sucursales desde el objeto de respuesta
                    SucursalResponse respuesta = response.body();
                    if (respuesta != null) {
                        sucursalList.clear(); // Limpiar la lista existente
                        sucursalList.addAll(respuesta.getSucursalList()); // Agregar los nuevos datos

                        // Notificar al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("API", "Error en la solicitud a la API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SucursalResponse> call, Throwable t) {
                Log.e("API", "Fallo en la solicitud a la API: " + t.getMessage(), t);
            }
        });
    }

    @Override
    public void onSucursalClick(int sucursalId, String sucursalNombre) {
        Log.d("SucursalesActivity", "onSucursalClick called with ID: " + sucursalId + ", Nombre: " + sucursalNombre);
        Sucursal sucursal = obtenerSucursalPorId(sucursalId); // Método para obtener la sucursal por su ID
        if (sucursal != null) {
            double latitud = Double.parseDouble(sucursal.getUbicacion().getLatitud());
            double longitud = Double.parseDouble(sucursal.getUbicacion().getLongitud());

            Intent intent = new Intent(this, VerSucursalActivity.class);
            intent.putExtra("sucursal_id", sucursalId);
            intent.putExtra("sucursal_nombre", sucursalNombre);
            intent.putExtra("latitud", latitud);
            intent.putExtra("longitud", longitud);
            startActivity(intent);
        } else {
            Log.e("SucursalesActivity", "Error: No se encontró la sucursal con ID: " + sucursalId);
        }
    }


    private Sucursal obtenerSucursalPorId(int id) {
        for (Sucursal sucursal : sucursalList) {
            if (sucursal.getIdSucursal() == id) {
                return sucursal;
            }
        }
        return null;
    }

}