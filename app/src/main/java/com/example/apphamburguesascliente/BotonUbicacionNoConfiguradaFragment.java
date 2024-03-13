package com.example.apphamburguesascliente;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BotonUbicacionNoConfiguradaFragment extends Fragment {


    private int idCliente = -1;

    private int TIPO_DE_UBICACION = -1;
    public BotonUbicacionNoConfiguradaFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boton_ubicacion_no_configurada, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        idCliente = sharedPreferences.getInt("id_cuenta", -1);

        // Obtener el tipo de ubicación de los argumentos del fragmento
        Bundle arguments = getArguments();
        if (arguments != null) {
            TIPO_DE_UBICACION = arguments.getInt("tipoUbicacion", -1);

        }


        // Accede al botón de añadir y añade un OnClickListener
        Button addButton = view.findViewById(R.id.addbtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad correspondiente para añadir

                Intent intent = new Intent(getActivity(), AnadirUbicacionActivity.class);
                intent.putExtra("idUsuario", idCliente);
                intent.putExtra("tipoUbicacion", TIPO_DE_UBICACION);
                startActivity(intent);

            }
        });

        return view;
    }
}