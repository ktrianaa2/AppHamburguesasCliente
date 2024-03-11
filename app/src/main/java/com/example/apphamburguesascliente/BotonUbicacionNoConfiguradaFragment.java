package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BotonUbicacionNoConfiguradaFragment extends Fragment {

    public BotonUbicacionNoConfiguradaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boton_ubicacion_no_configurada, container, false);

        // Obtén la ID del usuario desde los argumentos, si están disponibles
        Bundle args = getArguments();
        if (args != null && args.containsKey("idUsuario")) {
            int idUsuario = args.getInt("idUsuario");

            // Accede al botón de añadir y añade un OnClickListener
            Button addButton = view.findViewById(R.id.addbtn);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Abre la actividad correspondiente para añadir
                    Intent intent = new Intent(getActivity(), AnadirUbicacionActivity.class);
                    intent.putExtra("idUsuario", idUsuario);
                    startActivity(intent);
                }
            });
        } else {
            Log.e("BotonUbicacionNoConfiguradaFragment", "No se encontró 'idUsuario' en los argumentos del fragmento.");
        }

        return view;
    }
}