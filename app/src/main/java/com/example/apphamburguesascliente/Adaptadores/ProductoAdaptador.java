package com.example.apphamburguesascliente.Adaptadores;

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
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductoModelo product = productList.get(position);

        // Configurar todas las vistas necesarias
        holder.allMenuName.setText(product.getNombreProducto());
        holder.allMenuDescription.setText(product.getDescripcionProducto());
        holder.allMenuPrice.setText("$" + product.getPrecioProducto());

        // Puedes configurar la imagen si tienes una
        // holder.allMenuImage.setImageResource(product.getImageResource());

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
