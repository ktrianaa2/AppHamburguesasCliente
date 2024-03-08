package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CarritoCFragment extends Fragment {

    private int numeroProductosEnCarrito = 0;
    private int idCliente = -1; // Valor predeterminado o valor que consideres adecuado

    public CarritoCFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener el idCliente de los argumentos
        if (getArguments() != null) {
            idCliente = getArguments().getInt("idCliente", -1);
        }
    }

    public static CarritoCFragment newInstance(String nombreProducto, double precioProducto, String descripcionProducto) {
        CarritoCFragment fragment = new CarritoCFragment();
        Bundle args = new Bundle();
        args.putString("name", nombreProducto);
        args.putDouble("price", precioProducto);
        args.putString("description", descripcionProducto);
        fragment.setArguments(args);
        return fragment;
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
                    if (idCliente != -1) {
                        // Solo si tenemos un idCliente válido
                        Intent intent = new Intent(getActivity(), PaginaPrincipalActivity.class);
                        intent.putExtra("idCliente", idCliente);
                        startActivity(intent);
                    }
                }
            });
        }

        return view;
    }
}
