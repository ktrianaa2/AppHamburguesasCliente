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

public class BotonUbicacionConfiguradaFragment extends Fragment {

    private int TIPO_DE_UBICACION = -1;

    private int idCliente = -1; // Valor predeterminado o valor que consideres adecuado

    public BotonUbicacionConfiguradaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boton_ubicacion_configurada, container, false);

        // Obtener el idCliente desde SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        idCliente = sharedPreferences.getInt("id_cuenta", -1); // Usamos -1 como valor predeterminado si no se encuentra

        // Mostrar el idCliente en Logcat
        Log.d("BotonUbicacionConfig", "El idCliente es: " + idCliente);

        Bundle arguments = getArguments();
        if (arguments != null) {
            TIPO_DE_UBICACION = arguments.getInt("tipoUbicacion", -1);


        }


        double latitud = getArguments().getDouble("latitud");
        double longitud = getArguments().getDouble("longitud");

        // Configura el botón de editar y añade un OnClickListener
        Button editButton = view.findViewById(R.id.editbtn);
        editButton.setOnClickListener(v -> {
            // Abre la actividad correspondiente para la edición
            Intent intent = new Intent(getActivity(), EditarUbicacionActivity.class);
            intent.putExtra("idUsuario", idCliente);
            intent.putExtra("tipoUbicacion", TIPO_DE_UBICACION);
            intent.putExtra("latitud", latitud);
            intent.putExtra("longitud", longitud);
            startActivity(intent);
        });

        // Configura el botón de eliminar y añade un OnClickListener
        Button verButton = view.findViewById(R.id.verbtn);
        verButton.setOnClickListener(v -> {
            // Abre la actividad correspondiente para la visualizacion
            Intent intent = new Intent(getActivity(), VerUbicacionActivity.class);
            intent.putExtra("idUsuario", idCliente);
            intent.putExtra("tipoUbicacion", TIPO_DE_UBICACION);
            intent.putExtra("latitud", latitud);
            intent.putExtra("longitud", longitud);
            startActivity(intent);
        });

        return view;
    }
}