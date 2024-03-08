package com.example.apphamburguesascliente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BotonAnadirAlCarritoFragment extends Fragment {

    public BotonAnadirAlCarritoFragment() {
        // Required empty public constructor
    }

    public static BotonAnadirAlCarritoFragment newInstance(String nombreProducto, double precioProducto, String descripcionProducto) {
        BotonAnadirAlCarritoFragment fragment = new BotonAnadirAlCarritoFragment();
        Bundle args = new Bundle();
        args.putString("name", nombreProducto);
        args.putDouble("price", precioProducto);
        args.putString("description", descripcionProducto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boton_anadir_al_carrito, container, false);

        Button addButton = view.findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos del producto
                String nombreProducto = getArguments().getString("name");
                double precioProducto = getArguments().getDouble("price");
                String descripcionProducto = getArguments().getString("description");

                // Crear un nuevo fragmento de opciones de añadir al carrito con los datos del producto
                Fragment opcionesFragment = OpcionesAnadirAlCarritoFragment.newInstance(nombreProducto, precioProducto, descripcionProducto);

                // Cambiar al fragmento de opciones de añadir al carrito
                ((DetallesProductoComboActivity) requireActivity()).cambiarFragmento(opcionesFragment);
            }
        });


        return view;
    }
}
