package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


public class DetallesProductoComboActivity extends AppCompatActivity {
    ImageView imageView;
    TextView itemName, itemPrice;

    String name, price, rating, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto_combo);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        // price = intent.getStringExtra("price");
      //  imageUrl = intent.getStringExtra("image");

        imageView = findViewById(R.id.imageView5);
        itemName = findViewById(R.id.name);
        itemPrice = findViewById(R.id.price);

        itemName.setText(name);
        itemPrice.setText("$ "+price);


        ImageView flechaImageView = findViewById(R.id.imageView2);
        flechaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Esto hará que vuelva a la actividad anterior
            }
        });

        Button addToCartButton = findViewById(R.id.button);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mostrar el DialogFragment del carrito
                showCartDialog();
            }
        });
    }

    private void showCartDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AnadirAlCarritoFragment cartDialogFragment = new AnadirAlCarritoFragment();
        cartDialogFragment.show(fragmentManager, "cart_dialog");
    }
}