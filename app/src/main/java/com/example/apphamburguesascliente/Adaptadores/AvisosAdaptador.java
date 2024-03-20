package com.example.apphamburguesascliente.Adaptadores;

import android.content.Context;
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

import com.example.apphamburguesascliente.Modelos.AvisosModelo;
import com.example.apphamburguesascliente.R;

import java.util.List;

public class AvisosAdaptador extends RecyclerView.Adapter<AvisosAdaptador.ViewHolder> {

    private List<AvisosModelo.Aviso> listaAvisos;
    private Context context;

    public AvisosAdaptador(List<AvisosModelo.Aviso> listaAvisos, Context context) {
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
        AvisosModelo.Aviso aviso = listaAvisos.get(position);
        holder.tituloAviso.setText(aviso.getTitulo());
        holder.descripcionAviso.setText(aviso.getDescripcion());

        Log.d("AvisosAdaptador", "Posición: " + position + ", Titulo: " + aviso.getTitulo() + ", Descripción: " + aviso.getDescripcion());

        Log.d("AvisosAdaptador", "Cadena Base64 de imagen en posición " + position + ": " + aviso.getImagen());

        if (aviso.getImagen() != null && !aviso.getImagen().isEmpty()) {
            Log.d("AvisosAdaptador", "Imagen Base64 recibida en posición " + position + ": " + aviso.getImagen());
            byte[] decodedString = Base64.decode(aviso.getImagen(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imagenAviso.setImageBitmap(decodedByte);
            Log.d("AvisosAdaptador", "Imagen cargada en posición: " + position);
        } else {
            holder.imagenAviso.setImageResource(R.drawable.imagennotfound); // establece imagen predeterminada
            Log.d("AvisosAdaptador", "No hay imagen en posición: " + position);
        }
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

    public void actualizarDatos(List<AvisosModelo.Aviso> nuevosDatos) {
        if (nuevosDatos != null) {
            listaAvisos.clear();
            listaAvisos.addAll(nuevosDatos);
            notifyDataSetChanged();
        } else {
            Log.e("AvisosAdaptador", "Los nuevos datos son nulos");
        }
    }


}