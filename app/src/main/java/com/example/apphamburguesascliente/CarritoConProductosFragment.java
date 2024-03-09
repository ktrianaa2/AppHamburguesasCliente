package com.example.apphamburguesascliente;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphamburguesascliente.Adaptadores.CarritoAdaptador;
import com.example.apphamburguesascliente.Interfaces.OnProductAddedListener;
import com.example.apphamburguesascliente.Modelos.CarritoModelo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CarritoConProductosFragment extends Fragment implements OnProductAddedListener {


    private RecyclerView recyclerView;
    private CarritoAdaptador adaptador;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.carrito_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new CarritoAdaptador(new ArrayList<>());

        List<CarritoModelo.Producto> productosGuardados = obtenerProductosDesdeSharedPreferences();
        adaptador = new CarritoAdaptador(productosGuardados);

        adaptador.actualizarLista(productosGuardados);

        recyclerView.setAdapter(adaptador);
        // Recuperar la lista de productos desde SharedPreferences y actualizar el adaptador
        recyclerView.setAdapter(adaptador);
    }

    private List<CarritoModelo.Producto> obtenerProductosDesdeSharedPreferences() {
        // Obtener SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("carrito_preferences", MODE_PRIVATE);

        // Obtener la cadena JSON de productos desde SharedPreferences
        String productosJson = sharedPreferences.getString("productos", "");

        // Convertir la cadena JSON de productos a una lista de objetos CarritoModelo.Producto
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CarritoModelo.Producto>>() {}.getType();
        return gson.fromJson(productosJson, listType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carrito_con_productos, container, false);
    }

    @Override
    public void onProductAdded(String nombreProducto, double precioProducto, String descripcionProducto) {
        // Agregar el producto al carrito
        CarritoModelo carritoModel = CarritoModelo.getInstance();
        CarritoModelo.Producto producto = new CarritoModelo.Producto(nombreProducto, 1, precioProducto, 1);
        carritoModel.agregarProducto(producto);

        // Guardar la lista de productos en SharedPreferences
        guardarProductosEnSharedPreferences(carritoModel.getProductos());

        // Actualizar y mostrar los productos del carrito en el adaptador
        adaptador.actualizarLista(carritoModel.getProductos());
    }

    private void guardarProductosEnSharedPreferences(List<CarritoModelo.Producto> productos) {
        // Obtener SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("carrito_preferences", MODE_PRIVATE);

        // Convertir la lista de productos a una cadena JSON usando Gson
        Gson gson = new Gson();
        String productosJson = gson.toJson(productos);

        // Guardar la cadena JSON en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("productos", productosJson);
        editor.apply();
    }
}
