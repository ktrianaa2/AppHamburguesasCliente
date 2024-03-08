package com.example.apphamburguesascliente;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apphamburguesascliente.Adaptadores.CarritoAdapter;
import com.example.apphamburguesascliente.Adaptadores.ProductoAdaptador;
import com.example.apphamburguesascliente.Interfaces.OnProductAddedListener;
import com.example.apphamburguesascliente.Modelos.CarritoManager;
import com.example.apphamburguesascliente.Modelos.ProductoModelo;

import java.util.ArrayList;
import java.util.List;

public class CarritoConProductosFragment extends Fragment implements OnProductAddedListener {

    private RecyclerView recyclerView;
    private CarritoAdapter adaptador;
    private List<ProductoModelo> listaProductos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito_con_productos, container, false);
        recyclerView = view.findViewById(R.id.carrito_recycler);
        listaProductos = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador = new CarritoAdapter(getContext(), listaProductos);
        recyclerView.setAdapter(adaptador);
    }

    @Override
    public void onProductAdded(String nombreProducto, double precioProducto, String descripcionProducto) {
        ProductoModelo nuevoProducto = new ProductoModelo();
        nuevoProducto.setNombreProducto(nombreProducto);
        nuevoProducto.setPrecioProducto(precioProducto);
        nuevoProducto.setDescripcionProducto(descripcionProducto);

        listaProductos.add(nuevoProducto);
        adaptador.notifyDataSetChanged();
    }
}