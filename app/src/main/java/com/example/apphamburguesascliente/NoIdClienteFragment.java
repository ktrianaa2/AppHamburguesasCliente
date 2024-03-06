package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NoIdClienteFragment extends Fragment {

    private Button loginButton, registerButton;

    public NoIdClienteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_id_cliente, container, false);

        loginButton = view.findViewById(R.id.loginbtn);
        registerButton = view.findViewById(R.id.registerbtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de inicio de sesión
                Intent intent = new Intent(getActivity(), IniciarSesionActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de registro
                Intent intent = new Intent(getActivity(), RegistrarseActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}