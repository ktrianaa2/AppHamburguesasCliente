package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MisUbicacionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_ubicaciones);

        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);
        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y regresa a la anterior
            }
        });

        // Obtén la información sobre si la ubicación está configurada o no
        boolean ubicacionCasaConfigurada = obtenerUbicacionCasaConfigurada();
        boolean ubicacionTrabajoConfigurada = obtenerUbicacionTrabajoConfigurada();
        boolean ubicacionOtraConfigurada = obtenerUbicacionOtraConfigurada();

        // Carga el fragmento correspondiente
        cargarFragmento(ubicacionCasaConfigurada, ubicacionTrabajoConfigurada, ubicacionOtraConfigurada);
    }

    private void cargarFragmento(boolean ubicacionCasaConfigurada, boolean ubicacionTrabajoConfigurada, boolean ubicacionOtraConfigurada) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Reemplaza el fragmento de la casa en el contenedor según la ubicación configurada
        if (ubicacionCasaConfigurada) {
            fragmentTransaction.replace(R.id.fragmentBtnCasa, new BotonUbicacionConfiguradaFragment());
        } else {
            fragmentTransaction.replace(R.id.fragmentBtnCasa, new BotonUbicacionNoConfiguradaFragment());
        }

        // Reemplaza el fragmento del trabajo en el contenedor según la ubicación configurada
        if (ubicacionTrabajoConfigurada) {
            fragmentTransaction.replace(R.id.fragmentBtnTrabajo, new BotonUbicacionConfiguradaFragment());
        } else {
            fragmentTransaction.replace(R.id.fragmentBtnTrabajo, new BotonUbicacionNoConfiguradaFragment());
        }

        // Reemplaza el fragmento de otra en el contenedor según la ubicación configurada
        if (ubicacionOtraConfigurada) {
            fragmentTransaction.replace(R.id.fragmentBtnOtro, new BotonUbicacionConfiguradaFragment());
        } else {
            fragmentTransaction.replace(R.id.fragmentBtnOtro, new BotonUbicacionNoConfiguradaFragment());
        }

        fragmentTransaction.commit();
    }

    // Lógica para obtener la configuración de cada ubicación (casa, trabajo, otra)
    private boolean obtenerUbicacionCasaConfigurada() {
        // Implementa la lógica para obtener la información sobre la configuración de la ubicación de casa
        return false;
    }

    private boolean obtenerUbicacionTrabajoConfigurada() {
        // Implementa la lógica para obtener la información sobre la configuración de la ubicación de trabajo
        return false;
    }

    private boolean obtenerUbicacionOtraConfigurada() {
        // Implementa la lógica para obtener la información sobre la configuración de la ubicación otra
        return false;
    }
}