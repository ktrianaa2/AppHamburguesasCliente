package com.example.apphamburguesascliente.Modelos;

import com.google.gson.annotations.SerializedName;

public class UbicacionSucursal {
    @SerializedName("id_ubicacion")
    private int idUbicacion;

    private String latitud;

    private String longitud;

    @SerializedName("udescripcion")
    private String descripcion;


    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
