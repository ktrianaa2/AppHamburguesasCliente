package com.example.apphamburguesascliente;

import static java.sql.Types.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PaginaPrincipalActivity extends AppCompatActivity {

    private int idCliente = NULL;
    BottomNavigationView bottomNav;

    FragmentTransaction transaction;
    Fragment fragmentInicio, fragmentCarritoC, fragmentPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        // Intenta recibir idCliente, si no hay, queda como NULL.
        idCliente = getIntent().getIntExtra("idCliente", NULL);

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        fragmentInicio = new IniciooFragment();
        fragmentPerfil = new PerfillFragment();
        fragmentCarritoC = new CarritoCFragment();

        // Iniciar con el fragmento de inicio sin importar el estado de inicio de sesión.
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragmentInicio).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    transaction = getSupportFragmentManager().beginTransaction();

                    if (itemId == R.id.item_inicio) {
                        transaction.replace(R.id.fragmentContainer, fragmentInicio);
                    } else if (itemId == R.id.item_carrito) {
                        if (idCliente == NULL) {
                            // Si idCliente es nulo, cargar el NoIdClienteFragment para carrito
                            transaction.replace(R.id.fragmentContainer, new NoIdClienteFragment());
                        } else {
                            transaction.replace(R.id.fragmentContainer, fragmentCarritoC);
                        }
                    } else if (itemId == R.id.item_perfil) {
                        if (idCliente == NULL) {
                            // Si idCliente es nulo, cargar el NoIdClienteFragment para perfil
                            transaction.replace(R.id.fragmentContainer, new NoIdClienteFragment());
                        } else {
                            // Reemplazar con el fragmento PerfillFragment si el idCliente no es NULL
                            transaction.replace(R.id.fragmentContainer, fragmentPerfil);
                        }
                    }

                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
            };
}
