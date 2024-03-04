package com.example.apphamburguesascliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PaginaPrincipalActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    FragmentTransaction transaction;
    Fragment fragmentInicio, fragmentCarritoC, fragmentPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        fragmentInicio = new IniciooFragment();
        fragmentPerfil = new PerfillFragment();
        fragmentCarritoC = new CarritoCFragment();

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
                        transaction.replace(R.id.fragmentContainer, fragmentCarritoC);
                    } else if (itemId == R.id.item_perfil) {
                        transaction.replace(R.id.fragmentContainer, fragmentPerfil);
                    }

                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
            };

}