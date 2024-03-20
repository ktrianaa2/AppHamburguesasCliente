package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apphamburguesascliente.Adaptadores.AvisosAdaptador;
import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.AvisosModelo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnunciosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AvisosAdaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        recyclerView = findViewById(R.id.avisosRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adaptador = new AvisosAdaptador(new ArrayList<>(), this); // Inicializa el adaptador con una lista vacía
        recyclerView.setAdapter(adaptador); // Configura el adaptador

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

        Call<List<AvisosModelo>> call = apiService.getAvisos();
        call.enqueue(new Callback<List<AvisosModelo>>() {
            @Override
            public void onResponse(Call<List<AvisosModelo>> call, Response<List<AvisosModelo>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AnunciosActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<AvisosModelo> listaAvisos = response.body();
                adaptador.actualizarDatos(listaAvisos); // Actualiza los datos en el adaptador
            }

            @Override
            public void onFailure(Call<List<AvisosModelo>> call, Throwable t) {
                Log.e("AnunciosActivity", "Error al obtener los datos: " + t.getMessage());
            }
        });
    }
}
