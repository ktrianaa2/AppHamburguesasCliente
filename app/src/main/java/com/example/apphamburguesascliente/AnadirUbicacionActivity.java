package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import android.webkit.WebSettings;
import android.webkit.WebView;

public class AnadirUbicacionActivity extends AppCompatActivity {


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_ubicacion);

        ImageView imageViewFlecha = findViewById(R.id.flechaRetroceder);

        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y regresa a la anterior
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int idUsuario = extras.getInt("idUsuario", 0);

            // Imprimir la información en el Logcat
            Log.d("EditarUbicacionActivity", "ID de usuario obtenida: " + idUsuario);
        }

        webView = findViewById(R.id.webView);

        // Configurar WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Cargar archivo HTML que contiene el mapa Leaflet
        webView.loadUrl("file:///assets/map.html");
    }


}