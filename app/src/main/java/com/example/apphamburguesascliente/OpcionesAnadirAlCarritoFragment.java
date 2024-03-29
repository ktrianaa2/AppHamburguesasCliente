package com.example.apphamburguesascliente;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.apphamburguesascliente.Interfaces.OnProductAddedListener;

public class OpcionesAnadirAlCarritoFragment extends Fragment {

    private EditText quantityEditText;
    private int quantity = 1;
    private OnProductAddedListener productAddedListener;

    public OpcionesAnadirAlCarritoFragment() {
        // Required empty public constructor
    }

    public static OpcionesAnadirAlCarritoFragment newInstance(String nombreProducto, double precioProducto, String descripcionProducto, int puntosProducto) {
        OpcionesAnadirAlCarritoFragment fragment = new OpcionesAnadirAlCarritoFragment();
        Bundle args = new Bundle();
        args.putString("name", nombreProducto);
        args.putDouble("price", precioProducto);
        args.putString("description", descripcionProducto);
        args.putInt("points", puntosProducto);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnProductAddedListener) {
            productAddedListener = (OnProductAddedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnProductAddedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_opciones_anadir_al_carrito, container, false);

        quantityEditText = view.findViewById(R.id.quantityEditText);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button minusButton = view.findViewById(R.id.minusButton);
        Button plusButton = view.findViewById(R.id.plusButton);
        Button addButton = view.findViewById(R.id.AnadirButton);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null) {
                    String nombreProducto = getArguments().getString("name");
                    double precioProducto = getArguments().getDouble("price");
                    String descripcionProducto = getArguments().getString("description");
                    int puntosProducto = getArguments().getInt("points");

                    // Crear un nuevo fragmento BotonAnadirAlCarritoFragment y pasar los argumentos nuevamente
                    Fragment botonAnadirFragment = BotonAnadirAlCarritoFragment.newInstance(nombreProducto, precioProducto, descripcionProducto, puntosProducto);
                    ((DetallesProductoComboActivity) requireActivity()).cambiarFragmento(botonAnadirFragment);
                } else {
                    Log.e("OpcionesAnadirAlCarrito", "Bundle de argumentos es nulo");
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    quantityEditText.setText(String.valueOf(quantity));
                } else {
                    String nombreProducto = getArguments().getString("name");
                    double precioProducto = getArguments().getDouble("price");
                    String descripcionProducto = getArguments().getString("description");
                    int puntosProducto = getArguments().getInt("points");

                    // Crear un nuevo fragmento BotonAnadirAlCarritoFragment y pasar los argumentos nuevamente
                    Fragment botonAnadirFragment = BotonAnadirAlCarritoFragment.newInstance(nombreProducto, precioProducto, descripcionProducto, puntosProducto);
                    ((DetallesProductoComboActivity) requireActivity()).cambiarFragmento(botonAnadirFragment);                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityEditText.setText(String.valueOf(quantity));
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null) {
                    String nombreProducto = getArguments().getString("name");
                    double precioProducto = getArguments().getDouble("price");
                    String descripcionProducto = getArguments().getString("description");
                    int puntosProducto = getArguments().getInt("points");

                    int cantidad;
                    if (quantityEditText.getText().toString().isEmpty()) {
                        cantidad = 1;
                    } else {
                        cantidad = Integer.parseInt(quantityEditText.getText().toString());
                    }

                    // Imprimir los datos del producto en la consola, incluyendo la cantidad
                    Log.d("DetallesProducto Añadir Carrito", "Nombre: " + nombreProducto);
                    Log.d("DetallesProducto Añadir Carrito", "Precio: " + precioProducto);
                    Log.d("DetallesProducto Añadir Carrito", "Descripción: " + descripcionProducto);
                    Log.d("DetallesProducto Añadir Carrito", "Cantidad: " + cantidad);
                    Log.d("DetallesProducto Añadir Carrito", "Puntos: " + puntosProducto);

                    // Obtener idCliente de SharedPreferences
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    int idCliente = sharedPreferences.getInt("id_cuenta", -1); // Usar un valor por defecto si no se encuentra

                    // Si idCliente es válido, proceder a abrir PaginaPrincipalActivity
                    if (idCliente != -1) {
                        // Llamar al método de la Interface para pasar los datos
                        productAddedListener.onProductAdded(nombreProducto, precioProducto, descripcionProducto, cantidad, puntosProducto);
                        Toast.makeText(getContext(), "Agregado exitosamente al carrito", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), PaginaPrincipalActivity.class);
                        intent.putExtra("idCliente", idCliente); // Pasar el idCliente como extra
                        startActivity(intent);
                        getActivity().finish(); // Finalizar la actividad actual si deseas evitar que el usuario regrese a ella
                    } else {
                        // Manejar el caso de que no se encuentre el idCliente
                        Log.e("OpcionesAnadirAlCarrito", "Error al obtener el ID de cliente.");
                    }
                }
            }
        });
        return view;
    }
}
