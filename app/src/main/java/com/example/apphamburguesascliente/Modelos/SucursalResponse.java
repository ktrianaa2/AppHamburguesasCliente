package com.example.apphamburguesascliente.Modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SucursalResponse {
    @SerializedName("sucursales")
    private List<Sucursal> sucursalList;

    public List<Sucursal> getSucursalList() {
        return sucursalList;
    }

    public void setSucursalList(List<Sucursal> sucursalList) {
        this.sucursalList = sucursalList;
    }
}
