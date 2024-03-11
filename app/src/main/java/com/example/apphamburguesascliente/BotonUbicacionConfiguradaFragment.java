package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BotonUbicacionConfiguradaFragment extends Fragment {

    public BotonUbicacionConfiguradaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boton_ubicacion_configurada, container, false);

        // Obtén la ID del usuario y la ubicación desde los argumentos
        int idUsuario = getArguments().getInt("idUsuario");
        double latitud = getArguments().getDouble("latitud");
        double longitud = getArguments().getDouble("longitud");

        // Accede al botón de editar y añade un OnClickListener
        Button editButton = view.findViewById(R.id.editbtn);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad correspondiente para la edición
                Intent intent = new Intent(getActivity(), EditarUbicacionActivity.class);
                intent.putExtra("idUsuario", idUsuario);
                intent.putExtra("latitud", latitud);
                intent.putExtra("longitud", longitud);
                startActivity(intent);
            }
        });

        // En BotonUbicacionConfiguradaFragment y BotonUbicacionNoConfiguradaFragment
        Button deleteButton = view.findViewById(R.id.deletebtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar la lógica para eliminar la ubicación
                Log.d("EliminarUbicacion", "Eliminar ubicación para el usuario con ID: " + idUsuario);
            }
        });

        return view;
    }
}
