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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto_combo);

        Intent intent = getIntent();

        int idProducto = intent.getIntExtra("idProducto", 0);
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

        adaptador = new CarritoAdaptador(new ArrayList<>());
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

    private void mostrarProductosCarrito() {
        // Obtener la lista de productos del carrito
        CarritoModelo carritoModel = CarritoModelo.getInstance();
        List<CarritoModelo.Producto> productos = carritoModel.getProductos();

        // Crear un adaptador o realizar la lógica necesaria para mostrar los productos en tu interfaz de usuario
        // ...

        // Puedes actualizar la interfaz según la lista de productos del carrito
        // ...
    }

    private int generateProductId() {
        // Lógica para generar un ID único para el producto (puedes implementar según tus necesidades)
        // ...
        return 0;
    }


    @Override
    public void onProductAdded(String nombreProducto, double precioProducto, String descripcionProducto, int cantidad) {
        // Agregar el producto al carrito
        CarritoModelo carritoModel = CarritoModelo.getInstance();
        int productId = getIntent().getIntExtra("idProducto", 0); // Obtener el idProducto de los extras
        CarritoModelo.Producto producto = new CarritoModelo.Producto(nombreProducto, productId, precioProducto, cantidad);
        carritoModel.agregarProducto(producto);

        // Guardar la lista de productos en SharedPreferences
        guardarProductosEnSharedPreferences(carritoModel.getProductos());
        // Imprimir en Logcat para verificar que se guardaron los datos correctamente
        Log.d("Carrito", "Producto añadido: Nombre: " + producto.getNombre() + ", ID: " + producto.getId() + ", Precio: $" + producto.getPrecio());

        List<CarritoModelo.Producto> productos = carritoModel.getProductos();
        Log.d("Carrito", "Total de productos añadidos: " + productos.size());
        for (CarritoModelo.Producto p : productos) {
            Log.d("Carrito", "Nombre: " + p.getNombre() + ", ID: " + p.getId() + ", Precio: $" + p.getPrecio());
        }
        adaptador.actualizarLista(carritoModel.getProductos());
    }
}
