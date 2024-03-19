package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NotificacionesPerfilUbicacionesFragment extends Fragment {


    public NotificacionesPerfilUbicacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones_perfil_ubicaciones, container, false);

        Button vamosPerfilBtn = view.findViewById(R.id.vamosperfilbtn);
        vamosPerfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(intent);
            }
        });

        Button vamosUbicacionesBtn = view.findViewById(R.id.vamosubicacionesbtn);
        vamosUbicacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MisUbicacionesActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }
}