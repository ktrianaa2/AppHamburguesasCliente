package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphamburguesascliente.Adaptadores.CarritoAdaptador;
import com.example.apphamburguesascliente.Modelos.CarritoModelo;

import java.util.List;

public class CarritoCFragment extends Fragment {

    private int numeroProductosEnCarrito = 0;
    private int idCliente = -1; // Valor predeterminado o valor que consideres adecuado

    public CarritoCFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener el idCliente de los argumentos
        if (getArguments() != null) {
            idCliente = getArguments().getInt("idCliente", -1);
            // Mostrar el idCliente en Logcat
            Log.d("CarritoCFragment", "El idCliente es: " + idCliente);
        }
    }

    public static CarritoCFragment newInstance(String nombreProducto, double precioProducto, String descripcionProducto) {
        CarritoCFragment fragment = new CarritoCFragment();
        Bundle args = new Bundle();
        args.putString("name", nombreProducto);
        args.putDouble("price", precioProducto);
        args.putString("description", descripcionProducto);
        fragment.setArguments(args);
        return fragment;
    }

    List<CarritoModelo.Producto> listaDeProductos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        // Actualizar listaDeProductos desde el Singleton CarritoModelo
        listaDeProductos = CarritoModelo.getInstance().getProductos();
        // Actualizar numeroProductosEnCarrito basado en el tamaño de la lista
        numeroProductosEnCarrito = listaDeProductos != null ? listaDeProductos.size() : 0;

        if (numeroProductosEnCarrito > 0) {
            // Si hay productos en el carrito, cargar el fragmento con productos
            view = inflater.inflate(R.layout.fragment_carrito_con_productos, container, false);

            RecyclerView recyclerView = view.findViewById(R.id.carrito_recycler);
            CarritoAdaptador adaptador = new CarritoAdaptador(listaDeProductos);
            recyclerView.setAdapter(adaptador);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Obtener el precio total y mostrarlo en txtSubTotal
            TextView txtSubTotal = view.findViewById(R.id.txtSubTotal);
            double precioTotal = adaptador.calcularPrecioTotal();
            txtSubTotal.setText("Subtotal: $" + precioTotal);


            double subtotal = adaptador.calcularPrecioTotal(); // Ya lo tienes
            double iva = subtotal * 0.12; // Calcula el 12% de IVA del subtotal
            double totalAPagar = subtotal + iva; // Calcula el total a pagar

            // Actualiza el txtSubTotal, txtIva y txtAPagar
            txtSubTotal.setText("Subtotal: $" + String.format("%.2f", subtotal));

            TextView txtIva = view.findViewById(R.id.txtIva); // Asegúrate de tener este TextView en tu layout
            txtIva.setText("IVA: $" + String.format("%.2f", iva));

            TextView txtAPagar = view.findViewById(R.id.txtAPagar); // Asegúrate de tener este TextView en tu layout
            txtAPagar.setText("Total a pagar: $" + String.format("%.2f", totalAPagar));

            // Configurar el botón "Vaciar Carrito"
            Button clearButton = view.findViewById(R.id.clearButton);
            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Llamar al método para limpiar el carrito
                    limpiarCarrito();
                    // Notificar al adaptador que la lista cambió
                    adaptador.actualizarLista(listaDeProductos);
                    // Actualizar el contador de productos en el carrito
                    numeroProductosEnCarrito = listaDeProductos.size();
                    // Actualizar el precio total en txtSubTotal
                    double nuevoPrecioTotal = adaptador.calcularPrecioTotal();
                    txtSubTotal.setText("Subtotal: $" + nuevoPrecioTotal);
                }
            });

            // Boton Siguiente
            Button nextButton = view.findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Verificar si la lista de productos no está vacía
                    if (!listaDeProductos.isEmpty()) {
                        // Recorrer la lista de productos y mostrar sus detalles en consola
                        for (CarritoModelo.Producto producto : listaDeProductos) {
                            Log.d("Carrito Producto", "ID: " + producto.getId() + // Asegúrate de que tu clase Producto tenga el método getIdProducto()
                                    ", Nombre: " + producto.getNombre() +
                                    ", Precio: " + producto.getPrecio() +
                                    ", Cantidad: " + producto.getCantidad());
                        }
                    } else {
                        Log.d("Carrito", "El carrito está vacío.");
                    }

                    // Continuar con la siguiente actividad
                    Intent intent = new Intent(getActivity(), RealizarPagoActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            // Si el carrito está vacío, cargar el fragmento vacío con el botón "Ver Menú"
            view = inflater.inflate(R.layout.fragment_carrito_vacio, container, false);

            // Configurar el botón "Ver Menú"
            Button menuButton = view.findViewById(R.id.menubtn);
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (idCliente != -1) {
                        // Solo si tenemos un idCliente válido
                        Intent intent = new Intent(getActivity(), PaginaPrincipalActivity.class);
                        intent.putExtra("idCliente", idCliente);
                        startActivity(intent);
                    }
                }
            });
        }
        return view;
    }

    private void limpiarCarrito() {
        CarritoModelo carritoModelo = CarritoModelo.getInstance();
        carritoModelo.limpiarCarrito();
        listaDeProductos.clear();

        // Reemplazar el fragmento actual por el fragmento vacío
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new CarritoVacioFragment());
        transaction.commit();
    }

}
