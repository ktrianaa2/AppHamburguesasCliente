package com.example.apphamburguesascliente.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apphamburguesascliente.Modelos.AvisosModelo;
import com.example.apphamburguesascliente.R;

import java.util.List;

public class AvisosAdaptador extends RecyclerView.Adapter<AvisosAdaptador.ViewHolder> {

    private List<AvisosModelo> listaAvisos;
    private Context context;

    public AvisosAdaptador(List<AvisosModelo> listaAvisos, Context context) {
        this.listaAvisos = listaAvisos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.avisos_recycler_items, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AvisosModelo aviso = listaAvisos.get(position);
        holder.tituloAviso.setText(aviso.getTitulo());
        holder.descripcionAviso.setText(aviso.getDescripcion());

        // Cargar la imagen utilizando Glide
        // Glide.with(context)
        //        .load(aviso.getImagen()) // Aquí deberías pasar la URL o la ruta de la imagen
        //        .placeholder(R.drawable.imagennotfound) // Opcional: imagen de carga mientras se carga la imagen real
        //        .error(R.drawable.no_notificaciones) // Opcional: imagen de error si falla la carga de la imagen
        //        .into(holder.imagenAviso);
    }

    @Override
    public int getItemCount() {
        return listaAvisos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imagenAviso;
        TextView tituloAviso;
        TextView descripcionAviso;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenAviso = itemView.findViewById(R.id.imagenAviso);
            tituloAviso = itemView.findViewById(R.id.tituloAviso);
            descripcionAviso = itemView.findViewById(R.id.descripcionAviso);
        }
    }

    public void actualizarDatos(List<AvisosModelo> nuevosDatos) {
        listaAvisos.clear();
        listaAvisos.addAll(nuevosDatos);
        notifyDataSetChanged();
    }

}