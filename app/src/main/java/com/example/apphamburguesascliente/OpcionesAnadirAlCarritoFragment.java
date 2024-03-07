package com.example.apphamburguesascliente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class OpcionesAnadirAlCarritoFragment extends Fragment {


    private EditText quantityEditText;
    private int quantity = 1; // Valor inicial
    public OpcionesAnadirAlCarritoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_opciones_anadir_al_carrito, container, false);

        // Obtener referencias a los elementos de la interfaz de usuario
        quantityEditText = view.findViewById(R.id.quantityEditText);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button minusButton = view.findViewById(R.id.minusButton);
        Button plusButton = view.findViewById(R.id.plusButton);
        Button addButton = view.findViewById(R.id.AnadirButton);

        // Establecer colores de los botones
        minusButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        plusButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));

        // Configurar OnClickListener para el botón cancelar
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar al fragmento anterior
                ((DetallesProductoComboActivity) requireActivity()).cambiarFragmento(new BotonAnadirAlCarritoFragment());
            }
        });

        // Configurar OnClickListener para el botón de resta (-)
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restar 1 a la cantidad si es mayor que 1
                if (quantity > 1) {
                    quantity--;
                    quantityEditText.setText(String.valueOf(quantity));
                } else {
                    // Si la cantidad es 1, cambiar al fragmento de añadir al carrito
                    ((DetallesProductoComboActivity) requireActivity()).cambiarFragmento(new BotonAnadirAlCarritoFragment());
                }
            }
        });

        // Configurar OnClickListener para el botón de suma (+)
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sumar 1 a la cantidad
                quantity++;
                quantityEditText.setText(String.valueOf(quantity));
            }
        });

        // Configurar OnClickListener para el botón de añadir
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para reconocer que se presionó el botón de añadir
            }
        });

        return view;
    }
}
