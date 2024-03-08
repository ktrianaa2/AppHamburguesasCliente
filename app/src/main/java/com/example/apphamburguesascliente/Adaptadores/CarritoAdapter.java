package com.example.apphamburguesascliente.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphamburguesascliente.Modelos.ProductoModelo;
import com.example.apphamburguesascliente.R;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<ProductoModelo> listaProductos;
    private Context context;

    public CarritoAdapter(Context context, List<ProductoModelo> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.carrito_recycler_items, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        ProductoModelo producto = listaProductos.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class CarritoViewHolder extends RecyclerView.ViewHolder {
        // Aquí defines las vistas en tu item layout carrito_recycler_items.xml
        TextView nombreProducto, descripcionProducto, precioProducto;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.carrito_name);
            descripcionProducto = itemView.findViewById(R.id.carrito_description);
            precioProducto = itemView.findViewById(R.id.carrito_price);
        }

        public void bind(ProductoModelo producto) {
            nombreProducto.setText(producto.getNombreProducto());
            descripcionProducto.setText(producto.getDescripcionProducto());
            String precioFormateado = String.format("$%.2f", producto.getPrecioProducto());
            precioProducto.setText(precioFormateado);
        }
    }
}
