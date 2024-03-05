package com.example.apphamburguesascliente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apphamburguesascliente.Adaptadores.ProductoAdaptador;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.ProductoModelo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IniciooFragment extends Fragment {

    private ApiService apiService;
    private RecyclerView allMenuRecycler;
    private ProductoAdaptador adaptador;

    public IniciooFragment() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicioo, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cvpdjw94-8000.use2.devtunnels.ms/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // RecyclerView
        allMenuRecycler = view.findViewById(R.id.all_menu_recycler);
        adaptador = new ProductoAdaptador(new ArrayList<>());
        allMenuRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        allMenuRecycler.setAdapter(adaptador);

        // Obtener datos desde la API
        obtenerProductosDesdeAPI();
        return view;
    }

    private void obtenerProductosDesdeAPI() {
        Call<JsonObject> call = apiService.obtenerProductos();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    try {
                        JsonArray productosArray = jsonResponse.getAsJsonArray("productos");

                        if (productosArray != null && productosArray.size() > 0) {
                            List<ProductoModelo> listaProductos = ProductoModelo.fromJsonArray(productosArray);
                            actualizarLista(listaProductos);
                        }
                    } catch (com.google.gson.JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Manejar el error
                t.printStackTrace();
            }
        });
    }


    private void actualizarLista(List<ProductoModelo> listaProductos) {
        adaptador.setProductos(listaProductos);
    }

}