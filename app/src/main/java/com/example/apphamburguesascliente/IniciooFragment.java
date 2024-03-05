package com.example.apphamburguesascliente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apphamburguesascliente.Adaptadores.ProductoAdaptador;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.ProductoModelo;

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
                .baseUrl("https://nxcs4pj0-8000.use2.devtunnels.ms/")
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
        Call<List<ProductoModelo>> call = apiService.obtenerProductos();

        call.enqueue(new Callback<List<ProductoModelo>>() {
            @Override
            public void onResponse(Call<List<ProductoModelo>> call, Response<List<ProductoModelo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    actualizarLista(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ProductoModelo>> call, Throwable t) {
                // Manejar el error
            }
        });
    }

    private void actualizarLista(List<ProductoModelo> listaProductos) {
        adaptador.setProductos(listaProductos);
    }

}