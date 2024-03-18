package com.example.apphamburguesascliente;

import static java.sql.Types.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PaginaPrincipalActivity extends AppCompatActivity {

    private int idCliente = -1;
    BottomNavigationView bottomNav;

    FragmentTransaction transaction;
    Fragment fragmentInicio, fragmentCarritoC, fragmentPerfil, fragmentNotificaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        // Intenta recibir idCliente, si no hay, queda como -1.
        idCliente = getIntent().getIntExtra("idCliente", -1);

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        fragmentPerfil = new PerfillFragment();
        fragmentNotificaciones = new NotificacionesFragment();
        fragmentCarritoC = new CarritoCFragment();

        // Iniciar con el fragmento de inicio sin importar el estado de inicio de sesión.
        fragmentInicio = new IniciooFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragmentInicio).commit();

        // Inflar el menú según el estado de inicio de sesión
        if (idCliente == -1) {
            bottomNav.inflateMenu(R.menu.menu_bottom);
        } else {
            bottomNav.inflateMenu(R.menu.menu_bottom_user);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            transaction = getSupportFragmentManager().beginTransaction();

            if (itemId == R.id.item_inicio) {
                transaction.replace(R.id.fragmentContainer, fragmentInicio);
            } else if (itemId == R.id.item_carrito) {
                if (idCliente == -1) {
                    // Si idCliente es -1, cargar el NoIdClienteFragment para carrito
                    transaction.replace(R.id.fragmentContainer, new NoIdClienteFragment());
                } else {
                    fragmentCarritoC = new CarritoCFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("idCliente", idCliente); // Pasar el idCliente al fragmento
                    fragmentCarritoC.setArguments(bundle);
                    transaction.replace(R.id.fragmentContainer, fragmentCarritoC);
                }
            } else if (itemId == R.id.item_perfil) {
                if (idCliente == -1) {
                    // Si idCliente es -1, cargar el NoIdClienteFragment para perfil
                    transaction.replace(R.id.fragmentContainer, new NoIdClienteFragment());
                } else {
                    // Reemplazar con el fragmento PerfillFragment si el idCliente no es -1
                    transaction.replace(R.id.fragmentContainer, fragmentPerfil);
                }
            } else if (itemId == R.id.item_notificaciones) {
                if (idCliente == -1) {
                    // Si idCliente es -1, cargar el NoIdClienteFragment para notificaciones
                    transaction.replace(R.id.fragmentContainer, new NoIdClienteFragment());
                } else {
                    // Reemplazar con el fragmento NotificacionesFragment si el idCliente no es -1
                    transaction.replace(R.id.fragmentContainer, fragmentNotificaciones);
                }
            }

            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
    };

}
