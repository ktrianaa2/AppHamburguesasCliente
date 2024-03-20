package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apphamburguesascliente.Adaptadores.RecompensasAdaptador;
import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.ProductoModelo;
import com.example.apphamburguesascliente.Modelos.RecompensasModelo;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecompensasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecompensasAdaptador adaptador;

    private List<ProductoModelo> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recompensas);


        int idCliente = getIntent().getIntExtra("idCliente", -1);
        Log.d("RecompensasActivity", "El ID del cliente es: " + idCliente);

        if (idCliente != -1) {
            obtenerUsuario(idCliente);
        } else {
            // Si el ID del cliente es -1
            TextView txtPuntosT = findViewById(R.id.txtPuntosT);
            txtPuntosT.setText("Inicia Sesión para poder canjear estas recompensas");
        }


        listaProductos = new ArrayList<>();


        recyclerView = findViewById(R.id.recompensasRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adaptador = new RecompensasAdaptador(new ArrayList<>(), new ArrayList<>(), this);

        recyclerView.setAdapter(adaptador);

        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);

        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y regresa a la anterior
                finish();
            }
        });

        // Obtener la instancia de ApiService utilizando ApiClient
        ApiService apiService = ApiClient.getInstance();

        // Llamada para obtener la lista de productos
        Call<JsonObject> callProductos = apiService.obtenerProductos();
        callProductos.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Log.e("RecompensasActivity", "Error al obtener los productos: " + response.code());
                    return;
                }

                JsonObject productosJson = response.body();
                if (productosJson != null) {
                    // Convertir el JsonObject a una lista de ProductoModelo
                    JsonArray productosArray = productosJson.getAsJsonArray("productos");
                    listaProductos = ProductoModelo.fromJsonArray(productosArray); // Asignar los nuevos datos a la lista listaProductos
                    adaptador.setProductos(listaProductos);

                    // llamada para obtener  lista de recompensas
                    obtenerRecompensas();
                } else {
                    Log.e("RecompensasActivity", "La respuesta de productos es nula");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("RecompensasActivity", "Error al obtener los productos: " + t.getMessage());
            }
        });



    }


    private void obtenerRecompensas() {
        // Obtener la instancia de ApiService utilizando ApiClient
        ApiService apiService = ApiClient.getInstance();

        // Llamada para obtener la lista de recompensas
        Call<RecompensasModelo> callRecompensas = apiService.getRecompensas();
        callRecompensas.enqueue(new Callback<RecompensasModelo>() {
            @Override
            public void onResponse(Call<RecompensasModelo> call, Response<RecompensasModelo> response) {
                if (!response.isSuccessful()) {
                    Log.e("RecompensasActivity", "Error: " + response.code());
                    return;
                }

                RecompensasModelo recompensasModelo = response.body();
                List<RecompensasModelo.RecompensaProducto> listaRecompensas = recompensasModelo.getRecompensasProductos();
                if (listaRecompensas != null) {
                    adaptador.actualizarDatos(listaRecompensas); // Actualizar la lista de recompensas en el adaptador
                    Log.d("RecompensasActivity", "Cantidad de objetos recibidos: " + listaRecompensas.size());
                } else {
                    Log.e("RecompensasActivity", "La lista de recompensas es nula");
                }
            }

            @Override
            public void onFailure(Call<RecompensasModelo> call, Throwable t) {
                Log.e("RecompensasActivity", "Error al obtener los datos de recompensas: " + t.getMessage());
            }
        });
    }


    private void obtenerUsuario(int idCliente) {
        ApiService apiService = ApiClient.getInstance();
        Call<UserResponse> callUsuario = apiService.obtenerUsuario(String.valueOf(idCliente));
        callUsuario.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("RecompensasActivity", "Error al obtener el usuario: " + response.code());
                    return;
                }

                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    int puntos = Integer.parseInt(userResponse.getUsuario().getCpuntos());
                    TextView txtPuntosT = findViewById(R.id.txtPuntosT);
                    txtPuntosT.setText("Tienes un total de " + puntos + " puntos.");
                    obtenerRecompensas();
                } else {
                    Log.e("RecompensasActivity", "Respuesta de usuario nula");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("RecompensasActivity", "Error al obtener el usuario: " + t.getMessage());
            }
        });
    }

}
