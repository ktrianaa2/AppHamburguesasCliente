package com.example.apphamburguesascliente.Adaptadores;

import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphamburguesascliente.Api.ApiClient;
import com.example.apphamburguesascliente.CarritoCFragment;
import com.example.apphamburguesascliente.Interfaces.ApiService;
import com.example.apphamburguesascliente.Modelos.CarritoModelo;
import com.example.apphamburguesascliente.Modelos.ProductoModelo;
import com.example.apphamburguesascliente.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoAdaptador extends RecyclerView.Adapter<CarritoAdaptador.ViewHolder> {
    private List<CarritoModelo.Producto> productos;
    private List<ProductoModelo> todosLosProductos = new ArrayList<>();

    private CarritoCFragment fragment;



    public void actualizarLista(List<CarritoModelo.Producto> nuevosProductos) {
        productos.clear();
        productos.addAll(nuevosProductos);
        notifyDataSetChanged();
    }

    public CarritoAdaptador(List<CarritoModelo.Producto> productos, CarritoCFragment fragment) {
        this.productos = productos;
        this.fragment = fragment;
        obtenerTodosLosProductosDesdeAPI();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrito_recycler_items, parent, false);

        // Definir márgenes entre elementos programáticamente
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 20);
        view.setLayoutParams(layoutParams);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarritoModelo.Producto producto = productos.get(position);
        ProductoModelo pro = obtenerProducto(producto.getId());

        holder.txtNombreProducto.setText(producto.getNombre());

        // Calcular el precio total
        double precioTotal = producto.getPrecio() * producto.getCantidad();
        if (precioTotal < 0.01) {
            holder.txtPrecioTotal.setText("Free");
        } else {
            holder.txtPrecioTotal.setText("$" + precioTotal);
        }

        // Calcular los puntos
        int puntos = producto.getCantidad() * producto.getPuntos();
        String unidadesString = String.valueOf(producto.getCantidad());
        String puntosString = String.valueOf(puntos);

        if (producto.getPrecio() < 0.01) {
            holder.infoProducto.setText("Unidades: " + unidadesString + " Puntos: -" + puntosString);

        } else {
            String precioString = String.valueOf(producto.getPrecio());
            holder.infoProducto.setText("Precio: $" + precioString + " Unidades: " + unidadesString + " Puntos: " + puntosString);
        }


        if (pro != null) {

            if (pro.getImagen64() != null && !pro.getImagen64().isEmpty()) {
                Log.d("AvisosAdaptador", "Imagen Base64 recibida en posición " + position + ": " + pro.getImagen64());
                byte[] decodedString = Base64.decode(pro.getImagen64(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imagenProducto.setImageBitmap(decodedByte);
                Log.d("AvisosAdaptador", "Imagen cargada en posición: " + position);
            } else {
                holder.imagenProducto.setImageResource(R.drawable.imagennotfound);
                Log.d("AvisosAdaptador", "No hay imagen en posición: " + position);
            }

        }


        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la posición del elemento
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Remover el elemento de la lista
                    productos.remove(adapterPosition);
                    // Notificar al adaptador sobre el cambio en los datos
                    notifyItemRemoved(adapterPosition);
                    if (fragment != null) {
                        fragment.reloadData();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreProducto;
        TextView txtPrecioTotal;
        ImageView imagenProducto;
        TextView infoProducto;
        Button removeButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.carrito_name);
            txtPrecioTotal = itemView.findViewById(R.id.carrito_price_total);
            imagenProducto = itemView.findViewById(R.id.carrito_image);
            infoProducto = itemView.findViewById(R.id.carritoInfoProducto);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }

    public double calcularPrecioTotal() {
        double total = 0;
        for (CarritoModelo.Producto producto : productos) {
            total += producto.getPrecio() * producto.getCantidad();
        }
        return total;
    }

    public int calcularTotalPuntos() {
        int totalPuntos = 0;
        for (CarritoModelo.Producto producto : productos) {

            if (producto.getPrecio() < 0.01) {
                totalPuntos -= producto.getPuntos() * producto.getCantidad();
            } else {
                totalPuntos += producto.getPuntos() * producto.getCantidad();
            }
        }
        return totalPuntos;
    }


    public ProductoModelo obtenerProducto(int idProducto) {
        if (todosLosProductos != null) {
            for (ProductoModelo producto : todosLosProductos) {
                if (producto.getIdProducto() == idProducto) {
                    return producto;
                }
            }
        }
        return null;
    }


    private void obtenerTodosLosProductosDesdeAPI() {
        ApiService apiService = ApiClient.getInstance();
        Call<JsonObject> call = apiService.obtenerProductos();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    try {
                        JsonArray productosArray = jsonResponse.getAsJsonArray("productos");

                        if (productosArray != null && productosArray.size() > 0) {
                            todosLosProductos.clear(); // Borra la lista existente
                            todosLosProductos.addAll(ProductoModelo.fromJsonArray(productosArray));
                            notifyDataSetChanged();
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Manejar el error de la llamada a la API
                t.printStackTrace();
            }
        });
    }

}