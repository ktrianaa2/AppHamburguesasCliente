package com.example.apphamburguesascliente.Adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphamburguesascliente.Modelos.ProductoModelo;
import com.example.apphamburguesascliente.R;

import java.util.List;

public class ProductoAdaptador extends RecyclerView.Adapter<ProductoAdaptador.ProductViewHolder> {
    private List<ProductoModelo> productList;
    private OnItemClickListener listener;

    public ProductoAdaptador(List<ProductoModelo> productList) {
        this.productList = productList;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductoModelo producto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_recycler_items, parent, false);

        // Definir márgenes entre elementos programáticamente
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 20);
        view.setLayoutParams(layoutParams);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductoModelo product = productList.get(position);

        // Configurar todas las vistas necesarias
        holder.allMenuName.setText(product.getNombreProducto());
        holder.allMenuDescription.setText(product.getDescripcionProducto());
        holder.allMenuPrice.setText("$" + product.getPrecioProducto());

        if (product.getImagen64() != null && !product.getImagen64().isEmpty()) {
            Log.d("AvisosAdaptador", "Imagen Base64 recibida en posición " + position + ": " + product.getImagen64());
            byte[] decodedString = Base64.decode(product.getImagen64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.allMenuImage.setImageBitmap(decodedByte);
            Log.d("AvisosAdaptador", "Imagen cargada en posición: " + position);
        } else {
            holder.allMenuImage.setImageResource(R.drawable.imagennotfound); // establece imagen predeterminada
            Log.d("AvisosAdaptador", "No hay imagen en posición: " + position);
        }

        // Manejar clic en el elemento
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView allMenuName;
        TextView allMenuDescription;
        TextView allMenuPrice;
        ImageView allMenuImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            allMenuName = itemView.findViewById(R.id.all_menu_name);
            allMenuDescription = itemView.findViewById(R.id.all_menu_description);
            allMenuPrice = itemView.findViewById(R.id.all_menu_price);
            allMenuImage = itemView.findViewById(R.id.all_menu_image);
        }
    }

    public void setProductos(List<ProductoModelo> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }
}
