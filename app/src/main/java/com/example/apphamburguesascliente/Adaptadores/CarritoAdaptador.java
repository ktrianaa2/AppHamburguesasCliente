package com.example.apphamburguesascliente.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphamburguesascliente.Modelos.CarritoModelo;
import com.example.apphamburguesascliente.R;

import java.util.List;

public class CarritoAdaptador extends RecyclerView.Adapter<CarritoAdaptador.ViewHolder> {

    private List<CarritoModelo.Producto> productos;

    public void actualizarLista(List<CarritoModelo.Producto> nuevosProductos) {
        productos.clear();
        productos.addAll(nuevosProductos);
        notifyDataSetChanged();
    }

    public CarritoAdaptador(List<CarritoModelo.Producto> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrito_recycler_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarritoModelo.Producto producto = productos.get(position);

        holder.txtNombreProducto.setText(producto.getNombre());
        holder.txtPrecioProducto.setText("$" + producto.getPrecio());
        holder.txtUnidades.setText(String.valueOf(producto.getCantidad()));

        // Calcular el precio total
        double precioTotal = producto.getPrecio() * producto.getCantidad();
        holder.txtPrecioTotal.setText("$" + precioTotal); // Establecer el precio total
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Define las vistas necesarias para mostrar los datos
        TextView txtNombreProducto;
        TextView txtPrecioProducto;
        TextView txtUnidades; // Agregar el TextView para las unidades
        TextView txtPrecioTotal; // Agregar el TextView para el precio total

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.carrito_name);
            txtPrecioProducto = itemView.findViewById(R.id.carrito_price);
            txtUnidades = itemView.findViewById(R.id.carrito_unidades);
            txtPrecioTotal = itemView.findViewById(R.id.carrito_price_total); // Inicializar el TextView para el precio total
        }
    }
}
