package com.example.apphamburguesascliente.Adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apphamburguesascliente.Modelos.Sucursal;
import com.example.apphamburguesascliente.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SucursalAdaptador extends RecyclerView.Adapter<SucursalAdaptador.SucursalViewHolder> {

    private List<Sucursal> sucursalList;
    private OnSucursalClickListener clickListener;

    public SucursalAdaptador(List<Sucursal> sucursalList, OnSucursalClickListener clickListener) {
        this.sucursalList = sucursalList;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return sucursalList.size();
    }

    @NonNull
    @Override
    public SucursalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sucursales_recycler_items, parent, false);


        // Definir márgenes entre elementos programáticamente
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 20);
        view.setLayoutParams(layoutParams);


        SucursalViewHolder viewHolder = new SucursalViewHolder(view);
        return new SucursalViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SucursalViewHolder holder, int position) {
        Sucursal sucursal = sucursalList.get(position);
        holder.bind(sucursal);
        holder.setupMap(sucursal);
        holder.verButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onSucursalClick(sucursal.getIdSucursal(), sucursal.getRazonSocial());
                }
            }
        });
    }

    static class SucursalViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreTextView;
        private TextView direccionTextView;
        private MapView mapView;
        private GoogleMap googleMap;
        private TextView verButton;

        SucursalViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.sucursalNombre);
            direccionTextView = itemView.findViewById(R.id.sucursalDireccion);
            verButton = itemView.findViewById(R.id.verbtn);
            mapView = itemView.findViewById(R.id.mapView);
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
            }
        }

        void bind(Sucursal sucursal) {
            nombreTextView.setText(sucursal.getRazonSocial());
            String direccion = "Dirección: " + sucursal.getDireccion();
            direccionTextView.setText(direccion);
        }

        void setupMap(Sucursal sucursal) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;
                    googleMap.getUiSettings().setAllGesturesEnabled(false);
                    if (sucursal != null && sucursal.getUbicacion() != null) {
                        double latitud = Double.parseDouble(sucursal.getUbicacion().getLatitud());
                        double longitud = Double.parseDouble(sucursal.getUbicacion().getLongitud());
                        LatLng location = new LatLng(latitud, longitud);
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(location));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
                        Log.d("MapInfo", "ID: " + sucursal.getIdSucursal() +
                                ", Latitud: " + latitud +
                                ", Longitud: " + longitud);
                    }
                }
            });
        }
    }

    public interface OnSucursalClickListener {
        void onSucursalClick(int sucursalId, String sucursalNombre);
    }
}