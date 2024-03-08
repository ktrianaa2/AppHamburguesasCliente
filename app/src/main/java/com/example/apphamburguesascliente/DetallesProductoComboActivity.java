package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.apphamburguesascliente.Interfaces.OnProductAddedListener;

public class DetallesProductoComboActivity extends AppCompatActivity implements OnProductAddedListener {

    String name, price, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto_combo);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");

        double priceDouble = Double.parseDouble(price);

        TextView itemName = findViewById(R.id.name);
        TextView itemPrice = findViewById(R.id.price);
        TextView itemDescription = findViewById(R.id.description);

        itemName.setText(name);
        itemPrice.setText("$ " + priceDouble); // Mostrar el precio convertido
        itemDescription.setText(description);

        // Añade el fragmento BotonAnadirAlCarritoFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, BotonAnadirAlCarritoFragment.newInstance(name, priceDouble, description))
                    .commit();
        }
    }

    public void cambiarFragmento(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public void onProductAdded(String nombreProducto, double precioProducto, String descripcionProducto) {
        // Pasar los datos al CarritoCFragment
        Fragment carritoFragment = CarritoCFragment.newInstance(nombreProducto, precioProducto, descripcionProducto);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, carritoFragment)
                .commit();
    }
}
