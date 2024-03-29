package com.example.apphamburguesascliente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphamburguesascliente.Adaptadores.CarritoAdaptador;
import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.CarritoModelo;
import com.example.apphamburguesascliente.Modelos.DetalleProducto;
import com.example.apphamburguesascliente.Modelos.DetallesPedido;
import com.example.apphamburguesascliente.Modelos.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoCFragment extends Fragment {

    private int numeroProductosEnCarrito = 0;
    private int idCliente = -1;

    private int puntosUsuario;

    private double total = 0.00;

    View view;

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
            obtenerUsuarioDesdeAPI(idCliente);
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

        view = inflater.inflate(R.layout.fragment_carrito_c, container, false);


        // Actualizar listaDeProductos desde el Singleton CarritoModelo
        listaDeProductos = CarritoModelo.getInstance().getProductos();
        // Actualizar numeroProductosEnCarrito basado en el tamaño de la lista
        numeroProductosEnCarrito = listaDeProductos != null ? listaDeProductos.size() : 0;

        if (numeroProductosEnCarrito > 0) {

            // Si hay productos en el carrito, cargar el fragmento con productos
            view = inflater.inflate(R.layout.fragment_carrito_con_productos, container, false);
            actualizarDatos();

            RecyclerView recyclerView = view.findViewById(R.id.carrito_recycler);
            CarritoAdaptador adaptador = new CarritoAdaptador(listaDeProductos, this);
            recyclerView.setAdapter(adaptador);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



            // Obtener el precio total
            double subtotal = adaptador.calcularPrecioTotal();
            double iva = subtotal * 0.12;
            double totalAPagar = subtotal + iva;

            // Calcular el total de puntos
            int totalPuntos = adaptador.calcularTotalPuntos();



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
                }
            });

            // Boton Siguiente
            // Dentro del OnClickListener del botón "Siguiente"
            Button nextButton = view.findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Verificar si la lista de productos no está vacía
                    if (!listaDeProductos.isEmpty()) {
                        // Obtener el precio total
                        double subtotal = adaptador.calcularPrecioTotal();
                        double iva = subtotal * 0.12;
                        double totalAPagar = subtotal + iva;

                        // Calcular el total de puntos
                        int totalPuntos = adaptador.calcularTotalPuntos();

                        if ((totalPuntos + puntosUsuario) < 0) {
                            Toast.makeText(getActivity(), "Esta compra te deja puntos en negativo", Toast.LENGTH_SHORT).show();
                        } else {
                            // Continuar con el proceso de pago
                            // Crear una lista para los detalles del pedido
                            List<DetalleProducto> detallesPedido = new ArrayList<>();

                            // Recorrer la lista de productos y agregar cada uno como un DetalleProducto
                            for (CarritoModelo.Producto producto : listaDeProductos) {
                                DetalleProducto detalle = new DetalleProducto(
                                        producto.getId(), // Asegúrate de que este método devuelve un String o cambia el tipo en DetalleProducto
                                        producto.getCantidad(),
                                        producto.getPrecio()
                                );
                                detallesPedido.add(detalle);

                                Log.d("Carrito Producto", "ID: " + producto.getId() +
                                        ", Nombre: " + producto.getNombre() +
                                        ", Precio: " + producto.getPrecio() +
                                        ", Cantidad: " + producto.getCantidad());
                            }

                            // Crear el objeto DetallesPedido
                            DetallesPedido detallesPedidoObjeto = new DetallesPedido(detallesPedido);

                            // Crear intent para pasar datos a RealizarPagoActivity
                            if (total < 0.01) {
                                Intent intent = new Intent(getActivity(), RealizarPagoGratisActivity.class);
                                intent.putExtra("idCliente", idCliente);
                                intent.putExtra("totalPuntos", totalPuntos);
                                intent.putExtra("totalAPagar", totalAPagar);
                                intent.putExtra("detallesPedidoObjeto", detallesPedidoObjeto);
                                Log.d("Carrito", "Detalles del Pedido: " + detallesPedidoObjeto);
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(getActivity(), RealizarPagoActivity.class);
                                intent.putExtra("idCliente", idCliente);
                                intent.putExtra("totalPuntos", totalPuntos);
                                intent.putExtra("totalAPagar", totalAPagar);
                                intent.putExtra("detallesPedidoObjeto", detallesPedidoObjeto);
                                Log.d("Carrito", "Detalles del Pedido: " + detallesPedidoObjeto);
                                startActivity(intent);
                            }
                        }

                    } else {
                        Log.d("Carrito", "El carrito está vacío.");
                    }
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

    private void actualizarDatos() {
        // Actualizar listaDeProductos desde el Singleton CarritoModelo
        listaDeProductos = CarritoModelo.getInstance().getProductos();
        // Actualizar numeroProductosEnCarrito basado en el tamaño de la lista
        numeroProductosEnCarrito = listaDeProductos != null ? listaDeProductos.size() : 0;

        // Actualizar la vista del fragmento con los nuevos datos
        if (numeroProductosEnCarrito > 0) {

            // Configurar el RecyclerView y el adaptador nuevamente
            RecyclerView recyclerView = view.findViewById(R.id.carrito_recycler);
            CarritoAdaptador adaptador = new CarritoAdaptador(listaDeProductos, this);
            recyclerView.setAdapter(adaptador);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Obtener el precio total y mostrarlo en txtSubTotal
            TextView txtSubTotal = view.findViewById(R.id.txtSubTotal);
            double precioTotal = adaptador.calcularPrecioTotal();
            txtSubTotal.setText("Subtotal: $" + precioTotal);

            double subtotal = adaptador.calcularPrecioTotal();
            double iva = subtotal * 0.12;
            total = subtotal + iva;

            // Actualizar el txtSubTotal, txtIva y txtAPagar
            txtSubTotal.setText("Subtotal: $" + String.format("%.2f", subtotal));

            TextView txtIva = view.findViewById(R.id.txtIva);
            txtIva.setText("IVA: $" + String.format("%.2f", iva));

            TextView txtAPagar = view.findViewById(R.id.txtAPagar);
            txtAPagar.setText("Total a pagar: $" + String.format("%.2f", total));

            // Calcular el total de puntos y mostrarlo en txtPuntos
            int totalPuntos = adaptador.calcularTotalPuntos();
            TextView txtPuntos = view.findViewById(R.id.txtPuntos);
            txtPuntos.setText("Total de puntos: " + totalPuntos);
        } else {
            // Si el carrito está vacío, reemplazar el fragmento actual por el fragmento vacío
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, new CarritoVacioFragment());
            transaction.commit();
        }
    }


    public void reloadData() {
        // Llamar al método para actualizar los datos
        actualizarDatos();
    }

    private void obtenerUsuarioDesdeAPI(int idCliente) {
        int idCuenta = idCliente;

        if (idCuenta == -1) {
            Log.e("Recompensas", "id_cuenta no encontrado en SharedPreferences.");
            return; // No continuar si no tenemos un id_cuenta válido
        }

        ApiService apiService = ApiClient.getInstance();

        Call<UserResponse> callUsuario = apiService.obtenerUsuario(String.valueOf(idCuenta));
        callUsuario.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        // Obtener los puntos del usuario y almacenarlos en la variable puntosUsuario
                        puntosUsuario = Integer.parseInt(userResponse.getUsuario().getCpuntos());
                    } else {
                        Log.e("Error", "Respuesta de usuario nula");
                    }
                } else {
                    Log.e("Error", "Error al obtener los datos del usuario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Error", "Error en la solicitud de obtención de usuario: " + t.getMessage());
            }
        });
    }
}