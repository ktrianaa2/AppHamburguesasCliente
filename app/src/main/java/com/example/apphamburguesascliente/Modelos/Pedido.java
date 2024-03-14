package com.example.apphamburguesascliente.Modelos;

import com.google.gson.annotations.SerializedName;

public class Pedido {
    @SerializedName("id_cuenta")
    private int idCuenta;

    @SerializedName("cpuntos")
    private int puntos;

    @SerializedName("precio")
    private double precio;

    @SerializedName("tipo_de_pedido")
    private String tipoPedido;

    @SerializedName("metodo_de_pago")
    private String metodoPago;

    @SerializedName("id_sucursal")
    private int sucursal;

    @SerializedName("latitud")
    private String latitud;

    @SerializedName("longitud")
    private String longitud;

    @SerializedName("fecha_hora")
    private String fechaHora;

    @SerializedName("fecha_minutos")
    private String fechaMinutos;

    @SerializedName("detalles_pedido")
    private DetallesPedido detallesPedido;

    public Pedido(int idCuenta, int puntos, double precio, String tipoPedido, String metodoPago,
                  int sucursal, String latitud, String longitud, String fechaHora, String fechaMinutos,
                  DetallesPedido detallesPedido) {
        this.idCuenta = idCuenta;
        this.puntos = puntos;
        this.precio = precio;
        this.tipoPedido = tipoPedido;
        this.metodoPago = metodoPago;
        this.sucursal = sucursal;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaHora = fechaHora;
        this.fechaMinutos = fechaMinutos;
        this.detallesPedido = detallesPedido;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public int getIdSucursal() {
        return sucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.sucursal = sucursal;
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

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFechaMinutos() {
        return fechaMinutos;
    }

    public void setFechaMinutos(String fechaMinutos) {
        this.fechaMinutos = fechaMinutos;
    }

    public DetallesPedido getDetallesPedido() {
        return detallesPedido;
    }

    public void setDetallesPedido(DetallesPedido detallesPedido) {
        this.detallesPedido = detallesPedido;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idCuenta=" + idCuenta +
                ", puntos=" + puntos +
                ", precio=" + precio +
                ", tipoPedido='" + tipoPedido + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                ", idSucursal=" + sucursal +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                ", fechaMinutos='" + fechaMinutos + '\'' +
                ", detallesPedido=" + detallesPedido +
                '}';
    }
}
