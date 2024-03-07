package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class PerfillFragment extends Fragment {

    private Button btnEditar;
    private LinearLayout btnPuntos;
    private LinearLayout btnUbicaciones;
    private Button btnCerrarSesion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfill, container, false);

        // Encuentra las vistas
        btnEditar = view.findViewById(R.id.btnEditar);
        btnPuntos = view.findViewById(R.id.btnPuntos);
        btnUbicaciones = view.findViewById(R.id.btnUbicaciones);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        // Configura los clics
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad de edición de perfil
                Intent editarIntent = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(editarIntent);
            }
        });

        btnPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad de puntos
                Intent puntosIntent = new Intent(getActivity(), GestionPuntosActivity.class);
                startActivity(puntosIntent);
            }
        });

        btnUbicaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad de ubicaciones
                Intent ubicacionesIntent = new Intent(getActivity(), MisUbicacionesActivity.class);
                startActivity(ubicacionesIntent);
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la sesión y abre la actividad de inicio de sesión
                cerrarSesion();
            }
        });

        return view;
    }

    private void cerrarSesion() {
        // Aquí debes implementar la lógica para cerrar la sesión
        // Por ejemplo, puedes utilizar Firebase Auth, SharedPreferences, etc.

        // Después de cerrar la sesión, inicia la actividad de inicio de sesión
        Intent loginIntent = new Intent(getActivity(), IniciarSesionActivity.class);
        startActivity(loginIntent);
        // Asegúrate de que la actividad actual se cierre para que el usuario no pueda volver atrás
        getActivity().finish();
    }
}
