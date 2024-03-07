package com.example.apphamburguesascliente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BotonAnadirAlCarritoFragment extends Fragment {

    public BotonAnadirAlCarritoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boton_anadir_al_carrito, container, false);

        // Agrega un OnClickListener al botón en el Fragmento
        Button addButton = view.findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambia al fragmento de opciones al hacer clic en el botón
                ((DetallesProductoComboActivity) requireActivity()).cambiarFragmento(new OpcionesAnadirAlCarritoFragment());
            }
        });

        return view;
    }
}