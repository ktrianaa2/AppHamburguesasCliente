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

        adaptador = new AvisosAdaptador(new ArrayList<>(), this);
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

        Call<AvisosModelo> call = apiService.getAvisos();
        call.enqueue(new Callback<AvisosModelo>() {
            @Override
            public void onResponse(Call<AvisosModelo> call, Response<AvisosModelo> response) {
                if (!response.isSuccessful()) {
                    Log.e("AnunciosActivity", "Error: " + response.code());
                    return;
                }

                AvisosModelo avisosModelo = response.body();
                List<AvisosModelo.Aviso> listaAvisos = avisosModelo.getAvisosPrincipales();
                if (listaAvisos != null) {
                    adaptador.actualizarDatos(listaAvisos);
                    Log.d("AnunciosActivity", "Cantidad de objetos recibidos: " + listaAvisos.size());
                } else {
                    Log.e("AnunciosActivity", "La lista de avisos es nula");
                }
            }

            @Override
            public void onFailure(Call<AvisosModelo> call, Throwable t) {
                Log.e("AnunciosActivity", "Error al obtener los datos: " + t.getMessage());
            }
        });
    }
}
