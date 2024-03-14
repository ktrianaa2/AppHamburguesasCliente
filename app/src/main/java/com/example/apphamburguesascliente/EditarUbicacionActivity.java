package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class EditarUbicacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ubicacion);

        // Obtener datos enviados al iniciar la actividad
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int idUsuario = extras.getInt("idUsuario", 0);
            double latitud = extras.getDouble("latitud", 0.0);
            double longitud = extras.getDouble("longitud", 0.0);
            int tipoUbicacion = extras.getInt("tipoUbicacion", 0);

            // Imprimir la información en el Logcat
            Log.d("EditarUbicacionActivity", "ID de usuario obtenida: " + idUsuario);
            Log.d("EditarUbicacionActivity", "Tipo de Ubicacion obtenida: " + tipoUbicacion);
            Log.d("EditarUbicacionActivity", "Latitud obtenida: " + latitud);
            Log.d("EditarUbicacionActivity", "Longitud obtenida: " + longitud);
        }

        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);

        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y regresa a la anterior
                finish();
            }
        });
    }
}