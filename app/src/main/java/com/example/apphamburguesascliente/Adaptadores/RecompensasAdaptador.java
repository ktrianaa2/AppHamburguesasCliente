package com.example.apphamburguesascliente.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.DetallesProductoComboActivity;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.ProductoModelo;
import com.example.apphamburguesascliente.Modelos.RecompensasModelo;
import com.example.apphamburguesascliente.Modelos.User;
import com.example.apphamburguesascliente.Modelos.UserResponse;
import com.example.apphamburguesascliente.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecompensasAdaptador extends RecyclerView.Adapter<RecompensasAdaptador.RecompensaViewHolder> {

    private List<RecompensasModelo.RecompensaProducto> recompensas;
    private List<ProductoModelo> productos;
    private User usuario;
    private Context context;

    private int puntosUsuario;


    public RecompensasAdaptador(List<RecompensasModelo.RecompensaProducto> recompensas, List<ProductoModelo> productos, Context context, int idUsuario) {
        this.recompensas = recompensas;
        this.productos = productos;
        this.context = context;
        obtenerUsuarioDesdeAPI(idUsuario);
    }



    @NonNull
    @Override
    public RecompensaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recompensas_recycler_items, parent, false);
        return new RecompensaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecompensaViewHolder holder, int position) {
        RecompensasModelo.RecompensaProducto recompensa = recompensas.get(position);


        // Utiliza el ID del producto asociado para buscar y mostrar el nombre del producto
        String nombreProducto = obtenerNombreProducto(recompensa.getIdProducto());
        if (nombreProducto != null) {
            holder.recompensaNombre.setText(nombreProducto);
        } else {
            Log.e("RecompensasAdaptador", "No se encontró el nombre del producto para la recompensa en la posición: " + position);
        }

        holder.puntosRecompensa.setText(recompensa.getPuntosRecompensaProducto());

            ProductoModelo product = productos.get(position);


            if (product.getImagen64() != null && !product.getImagen64().isEmpty()) {
                Log.d("AvisosAdaptador", "Imagen Base64 recibida en posición " + position + ": " + product.getImagen64());
                byte[] decodedString = Base64.decode(product.getImagen64(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.recompensaImagen.setImageBitmap(decodedByte);
                Log.d("AvisosAdaptador", "Imagen cargada en posición: " + position);
            } else {
                holder.recompensaImagen.setImageResource(R.drawable.imagennotfound); // establece imagen predeterminada
                Log.d("AvisosAdaptador", "No hay imagen en posición: " + position);
            }


        holder.canjearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // Obtener la posición del adaptador

                if (adapterPosition != RecyclerView.NO_POSITION) { // Verificar si la posición es válida
                    RecompensasModelo.RecompensaProducto recompensa = recompensas.get(adapterPosition);
                    int puntosRequeridos = Integer.parseInt(recompensa.getPuntosRecompensaProducto());

                    if (puntosUsuario >= puntosRequeridos) {
                        ProductoModelo producto = productos.get(adapterPosition);
                        int puntos = Integer.parseInt(recompensa.getPuntosRecompensaProducto());

                        // Crear un Intent para abrir la actividad de detalles del producto
                        Intent intent = new Intent(context, DetallesProductoComboActivity.class);
                        intent.putExtra("idProducto", producto.getIdProducto());
                        intent.putExtra("name", producto.getNombreProducto());
                        intent.putExtra("price", "0.00");
                        intent.putExtra("description", producto.getDescripcionProducto());
                        intent.putExtra("points", puntos);
                        intent.putExtra("imagen", producto.getImagen64());
                        context.startActivity(intent);
                    } else {
                        // Si los puntos del usuario son insuficientes, mostrar un mensaje
                        Toast.makeText(context, "No tienes suficientes puntos para canjear este producto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return recompensas.size();
    }

    // Método para actualizar los datos del adaptador con una nueva lista de recompensas de productos
    public void actualizarDatos(List<RecompensasModelo.RecompensaProducto> nuevaLista) {
        recompensas.clear();
        recompensas.addAll(nuevaLista);
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
    }

    // Método para obtener el nombre del producto a partir de su ID
    private String obtenerNombreProducto(int idProducto) {
        Log.d("RecompensasAdaptador", "Buscando producto con ID: " + idProducto);
        for (ProductoModelo producto : productos) {
            if (producto.getIdProducto() == idProducto) {
                Log.d("RecompensasAdaptador", "Producto encontrado con ID: " + idProducto + ", Nombre: " + producto.getNombreProducto());
                return producto.getNombreProducto();
            }
        }
        Log.e("RecompensasAdaptador", "No se encontró el nombre del producto para el ID: " + idProducto);
        return "Nombre del producto unu";
    }

    public void setProductos(List<ProductoModelo> listaProductos) {
        this.productos = listaProductos;
        notifyDataSetChanged();
    }



    public static class RecompensaViewHolder extends RecyclerView.ViewHolder {
        ImageView recompensaImagen;
        TextView recompensaNombre;
        TextView puntosRecompensa;
        Button canjearButton;

        public RecompensaViewHolder(@NonNull View itemView) {
            super(itemView);
            recompensaImagen = itemView.findViewById(R.id.recompensaImagen);
            recompensaNombre = itemView.findViewById(R.id.recompensaNombre);
            puntosRecompensa = itemView.findViewById(R.id.puntosRecompensa);
            canjearButton = itemView.findViewById(R.id.canjearButton);
        }
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
                        notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
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
