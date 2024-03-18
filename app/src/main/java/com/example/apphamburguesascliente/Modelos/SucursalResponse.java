package com.example.apphamburguesascliente.Modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SucursalResponse {
    @SerializedName("sucursal")
    private List<Sucursal> sucursalList;

    @SerializedName("sucursales")
    private List<Sucursal> sucursalesList;

    // Este método intenta devolver siempre la lista correcta, priorizando 'sucursalesList'.
    public List<Sucursal> getSucursalList() {
        // Si 'sucursalesList' no es null, lo devuelve.
        if (sucursalesList != null) {
            return sucursalesList;
        }
        // Si 'sucursalesList' es null, intenta devolver 'sucursalList'.
        // Esto también será null si 'sucursalList' no se ha inicializado.
        return sucursalList;
    }

    public void setSucursalList(List<Sucursal> sucursalList) {
        this.sucursalList = sucursalList;
    }

    // Opcionalmente, puedes añadir un setter para 'sucursalesList' si necesitas
    // establecer esta lista desde otra parte del código, aunque normalmente esto
    // se manejará a través de la deserialización de Gson.
    public void setSucursalesList(List<Sucursal> sucursalesList) {
        this.sucursalesList = sucursalesList;
    }
}
