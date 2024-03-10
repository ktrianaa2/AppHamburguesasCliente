package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apphamburguesascliente.Adaptadores.ComboAdaptador;
import com.example.apphamburguesascliente.Adaptadores.ProductoAdaptador;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.ComboModelo;
import com.example.apphamburguesascliente.Modelos.ProductoModelo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IniciooFragment extends Fragment implements ProductoAdaptador.OnItemClickListener {

    private ApiService apiService;
    private RecyclerView allMenuRecycler;
    private ProductoAdaptador adaptador;

    private RecyclerView combosRecycler;
    private ComboAdaptador comboAdaptador;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicioo, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://9jpn4ctd-8000.use2.devtunnels.ms/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        CardView cardEmpresa = view.findViewById(R.id.cardEmpresa);
        CardView cardRecompensas = view.findViewById(R.id.cardRecompensas);
        CardView cardSucursales = view.findViewById(R.id.cardSucursales);
        CardView cardAnuncios = view.findViewById(R.id.cardAnuncios);
        CardView cardMasInformacion = view.findViewById(R.id.cardMasInformacion);

        cardEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmpresaActivity.class);
                startActivity(intent);
            }
        });

        cardRecompensas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecompensasActivity.class);
                startActivity(intent);
            }
        });

        cardSucursales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SucursalesActivity.class);
                startActivity(intent);
            }
        });

        cardAnuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnunciosActivity.class);
                startActivity(intent);
            }
        });

        cardMasInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MasInformacionActivity.class);
                startActivity(intent);
            }
        });

        //Productos
        allMenuRecycler = view.findViewById(R.id.all_menu_recycler);
        adaptador = new ProductoAdaptador(new ArrayList<>());
        allMenuRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        allMenuRecycler.setAdapter(adaptador);

        adaptador.setOnItemClickListener(this);
        obtenerProductosDesdeAPI();


        // Combos
        combosRecycler = view.findViewById(R.id.combos_recycler);
        comboAdaptador = new ComboAdaptador(new ArrayList<>(), getActivity());
        combosRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        combosRecycler.setAdapter(comboAdaptador);

        obtenerCombosDesdeAPI();


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

                            for (ProductoModelo producto : listaProductos) {
                                Log.d("Producto", "ID: " + producto.getIdProducto());
                                Log.d("Producto", "Nombre: " + producto.getNombreProducto());
                                Log.d("Producto", "Precio: " + producto.getPrecioProducto());
                                Log.d("Producto", "Descripcion: " + producto.getDescripcionProducto());

                            }
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
    @Override
    public void onItemClick(ProductoModelo producto) {
        Intent intent = new Intent(getActivity(), DetallesProductoComboActivity.class);
        intent.putExtra("name", producto.getNombreProducto());
        intent.putExtra("price", String.valueOf(producto.getPrecioProducto())); // Convertir a String
        intent.putExtra("description", producto.getDescripcionProducto());
        startActivity(intent);
    }


    private void obtenerCombosDesdeAPI() {
        Call<JsonObject> call = apiService.obtenerCombos();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    try {
                        JsonArray combosArray = jsonResponse.getAsJsonArray("combos");

                        if (combosArray != null && combosArray.size() > 0) {
                            List<ComboModelo.Combo> listaCombos = ComboModelo.fromJsonArray(combosArray);

                            for (ComboModelo.Combo combo : listaCombos) {
                                Log.d("Combo", "ID: " + combo.getIdCombo());
                                Log.d("Combo", "Nombre: " + combo.getNombreCb());
                                Log.d("Combo", "Precio: " + combo.getPrecioUnitario());
                                Log.d("Combo", "Descripción: " + combo.getDescripcionCombo());
                            }

                            actualizarListaCombos(listaCombos);
                        } else {
                            Log.d("Combo", "No se recibieron combos desde la API");
                        }
                    } catch (com.google.gson.JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("Combo", "Error en la respuesta de la API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Manejar el error
                Log.e("Combo", "Error al obtener combos desde la API", t);
            }
        });
    }

    private void actualizarListaCombos(List<ComboModelo.Combo> listaCombos) {
        comboAdaptador.setCombos(listaCombos);
    }

}