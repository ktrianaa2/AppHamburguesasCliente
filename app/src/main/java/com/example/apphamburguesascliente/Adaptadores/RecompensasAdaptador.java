package com.example.apphamburguesascliente.Adaptadores;

import android.content.Context;
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

import com.example.apphamburguesascliente.Modelos.ProductoModelo;
import com.example.apphamburguesascliente.Modelos.RecompensasModelo;
import com.example.apphamburguesascliente.R;

import java.util.List;

public class RecompensasAdaptador extends RecyclerView.Adapter<RecompensasAdaptador.RecompensaViewHolder> {

    private List<RecompensasModelo.RecompensaProducto> recompensas;
    private List<ProductoModelo> productos;
    private Context context;

    public RecompensasAdaptador(List<RecompensasModelo.RecompensaProducto> recompensas, List<ProductoModelo> productos, Context context) {
        this.recompensas = recompensas;
        this.productos = productos;
        this.context = context;
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
                // Lógica para canjear la recompensa
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
}
