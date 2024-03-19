package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NotificacionesUbicacionesPedidosFragment extends Fragment {



    public NotificacionesUbicacionesPedidosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notificaciones_ubicaciones_pedidos, container, false);

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