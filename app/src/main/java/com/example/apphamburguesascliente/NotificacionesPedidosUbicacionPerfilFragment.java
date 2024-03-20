package com.example.apphamburguesascliente;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NotificacionesPedidosUbicacionPerfilFragment extends Fragment {


    public NotificacionesPedidosUbicacionPerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notificaciones_pedidos_ubicacion_perfil, container, false);

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

                // Obtener la ID de usuario
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                int idCuenta = sharedPreferences.getInt("id_cuenta", 0);

                // Abrir la actividad de "Mis Ubicaciones" con la ID de usuario como extra
                Intent intent = new Intent(getActivity(), MisUbicacionesActivity.class);
                intent.putExtra("idUsuario", idCuenta);
                startActivity(intent);
            }
        });


        return view;

    }
}