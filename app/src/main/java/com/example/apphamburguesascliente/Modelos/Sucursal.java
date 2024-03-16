package com.example.apphamburguesascliente.Modelos;

import com.google.gson.annotations.SerializedName;

public class Sucursal {
    @SerializedName("id_sucursal")
    private int idSucursal;

    @SerializedName("srazon_social")
    private String razonSocial;

    @SerializedName("sdireccion")
    private String direccion;

    @SerializedName("id_ubicacion")
    private UbicacionSucursal ubicacion;



    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }


    public UbicacionSucursal getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionSucursal ubicacion) {
        this.ubicacion = ubicacion;
    }


}
