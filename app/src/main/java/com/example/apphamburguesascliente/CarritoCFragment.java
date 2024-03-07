package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class CarritoCFragment extends Fragment {

    private int numeroProductosEnCarrito = 0;


    public CarritoCFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        if (numeroProductosEnCarrito > 0) {
            // Si hay productos en el carrito, cargar el fragmento con productos
            view = inflater.inflate(R.layout.fragment_carrito_con_productos, container, false);
        } else {
            // Si el carrito está vacío, cargar el fragmento vacío
            view = inflater.inflate(R.layout.fragment_carrito_vacio, container, false);

            // Configurar el botón "Ver Menú"
            Button menuButton = view.findViewById(R.id.menubtn);
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PaginaPrincipalActivity.class);
                    startActivity(intent);
                }
            });
        }

        Button clearButton = view.findViewById(R.id.clearButton);
        Button nextButton = view.findViewById(R.id.nextButton);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para vaciar el carrito (llenar el contenedor, etc.)
                // Actualizar el número de productos en el carrito
                numeroProductosEnCarrito = 0;

                // Recargar el fragmento vacío
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new CarritoVacioFragment()).commit();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir otra actividad
                Intent intent = new Intent(getActivity(), RealizarPagoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}