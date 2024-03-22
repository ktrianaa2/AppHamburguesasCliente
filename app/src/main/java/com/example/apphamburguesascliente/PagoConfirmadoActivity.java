package com.example.apphamburguesascliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class PagoConfirmadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_confirmado);
        int idCliente = getIntent().getIntExtra("idCliente", -1);

        // Configurar el listener para el botón "Menú"
        MaterialButton menuButton = findViewById(R.id.menubtn);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PagoConfirmadoActivity.this, PaginaPrincipalActivity.class);
                intent.putExtra("idCliente", idCliente);
                startActivity(intent);
            }
        });

        // Configurar el listener para el botón "Ver Estado del Pedido"
        MaterialButton pedidoButton = findViewById(R.id.pedidobtn);
        pedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí implementar la lógica para ver el estado del pedido
            }
        });

    }
}