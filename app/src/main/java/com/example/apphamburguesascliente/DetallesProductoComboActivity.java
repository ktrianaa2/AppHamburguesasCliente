package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class DetallesProductoComboActivity extends AppCompatActivity {
    ImageView imageView;
    TextView itemName, itemPrice, itemDescription;

    String name, price, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto_combo);

        Intent intent = getIntent();

        ImageView imageViewFlecha = findViewById(R.id.imageView2);

        imageViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y regresa a la anterior
                finish();
            }
        });

        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");

        // Convertir el precio de String a double
        double priceDouble = Double.parseDouble(price);

        // Resto del código
        itemName = findViewById(R.id.name);
        itemPrice = findViewById(R.id.price);
        itemDescription = findViewById(R.id.description);

        itemName.setText(name);
        itemPrice.setText("$ " + priceDouble); // Mostrar el precio convertido
        itemDescription.setText(description);


        // Fragment
        // Inicializa el Fragmento por defecto
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new BotonAnadirAlCarritoFragment())
                .commit();

    }

    public void cambiarFragmento(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

}