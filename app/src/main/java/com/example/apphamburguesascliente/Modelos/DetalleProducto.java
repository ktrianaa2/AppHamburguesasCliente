package com.example.apphamburguesascliente.Modelos;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DetalleProducto implements Serializable {
    @SerializedName("id_producto")
    private int idProducto;

    @SerializedName("cantidad_pedido")
    private int cantidadPedido;

    @SerializedName("costo_unitario")
    private double costoUnitario;

    public DetalleProducto(int idProducto, int cantidadPedido, double costoUnitario) {
        this.idProducto = idProducto;
        this.cantidadPedido = cantidadPedido;
        this.costoUnitario = costoUnitario;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidadPedido() {
        return cantidadPedido;
    }

    public void setCantidadPedido(int cantidadPedido) {
        this.cantidadPedido = cantidadPedido;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }
}
