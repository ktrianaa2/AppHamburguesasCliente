package com.example.apphamburguesascliente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.apphamburguesascliente.Adaptadores.CarritoAdaptador;
import com.example.apphamburguesascliente.Interfaces.OnProductAddedListener;
import com.example.apphamburguesascliente.Modelos.CarritoModelo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DetallesProductoComboActivity extends AppCompatActivity implements OnProductAddedListener {

    String name, price, description;
    private CarritoAdaptador adaptador;

    private CarritoCFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto_combo);

        Intent intent = getIntent();

        int idProducto = intent.getIntExtra("idProducto", 0);
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");
        int puntosProducto = intent.getIntExtra("points", 0);

        double priceDouble = Double.parseDouble(price);

        TextView itemName = findViewById(R.id.name);
        TextView itemPrice = findViewById(R.id.price);
        TextView itemDescription = findViewById(R.id.description);

        itemName.setText(name);
        itemPrice.setText("$ " + priceDouble);
        itemDescription.setText(description);

        CarritoCFragment carritoFragment = (CarritoCFragment) getSupportFragmentManager().findFragmentByTag("CarritoCFragmentTag");
        adaptador = new CarritoAdaptador(new ArrayList<>(), carritoFragment);


        // Añade el fragmento BotonAnadirAlCarritoFragment, asegurándote de pasar los puntosProducto como argumento
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, BotonAnadirAlCarritoFragment.newInstance(name, priceDouble, description, puntosProducto))
                    .commit();
        }
    }

    public void cambiarFragmento(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void guardarProductosEnSharedPreferences(List<CarritoModelo.Producto> productos) {
        // Obtener SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("carrito_preferences", MODE_PRIVATE);

        // Convertir la lista de productos a una cadena JSON usando Gson
        Gson gson = new Gson();
        String productosJson = gson.toJson(productos);

        // Guardar la cadena JSON en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("productos", productosJson);
        editor.apply();
    }

    @Override
    public void onProductAdded(String nombreProducto, double precioProducto, String descripcionProducto, int cantidad, int puntosProducto) {
        // Agregar el producto al carrito
        CarritoModelo carritoModel = CarritoModelo.getInstance();
        int productId = getIntent().getIntExtra("idProducto", 0);
        String claveProducto = productId + "_" + precioProducto;

        // Verificar si el producto ya existe en el carrito
        List<CarritoModelo.Producto> productosEnCarrito = carritoModel.getProductos();
        for (CarritoModelo.Producto producto : productosEnCarrito) {
            if (producto.getClave().equals(claveProducto)) {
                // Si el producto ya existe, simplemente actualiza la cantidad
                carritoModel.actualizarCantidadProducto(claveProducto, cantidad);
                // Guardar la lista de productos en SharedPreferences
                guardarProductosEnSharedPreferences(productosEnCarrito);
                // Actualizar la interfaz de usuario
                adaptador.actualizarLista(productosEnCarrito);
                return;
            }
        }

        // Si el producto no existe, agrégalo al carrito
        CarritoModelo.Producto producto = new CarritoModelo.Producto(nombreProducto, productId, precioProducto, cantidad, puntosProducto, claveProducto);
        carritoModel.agregarProducto(producto);

        // Guardar la lista de productos en SharedPreferences
        guardarProductosEnSharedPreferences(carritoModel.getProductos());

        // Actualizar la interfaz de usuario
        adaptador.actualizarLista(carritoModel.getProductos());
    }


}