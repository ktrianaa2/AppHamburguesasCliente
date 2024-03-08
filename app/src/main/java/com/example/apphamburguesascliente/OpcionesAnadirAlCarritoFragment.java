package com.example.apphamburguesascliente;

import android.content.Context;
import android.os.Bundle;

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

    public static OpcionesAnadirAlCarritoFragment newInstance(String nombreProducto, double precioProducto, String descripcionProducto) {
        OpcionesAnadirAlCarritoFragment fragment = new OpcionesAnadirAlCarritoFragment();
        Bundle args = new Bundle();
        args.putString("name", nombreProducto);
        args.putDouble("price", precioProducto);
        args.putString("description", descripcionProducto);
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

        minusButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        plusButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DetallesProductoComboActivity) requireActivity()).cambiarFragmento(new BotonAnadirAlCarritoFragment());
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    quantityEditText.setText(String.valueOf(quantity));
                } else {
                    ((DetallesProductoComboActivity) requireActivity()).cambiarFragmento(new BotonAnadirAlCarritoFragment());
                }
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

                    // Llamar al método de la Interface para pasar los datos
                    productAddedListener.onProductAdded(nombreProducto, precioProducto, descripcionProducto);
                }
            }

        });
        return view;
    }
}
