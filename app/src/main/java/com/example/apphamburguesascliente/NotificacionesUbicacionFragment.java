package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NotificacionesUbicacionFragment extends Fragment {


    public NotificacionesUbicacionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones_ubicacion, container, false);

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