package com.example.apphamburguesascliente.Interfaces;

public interface OnProductAddedListener {
    void onProductAdded(String nombreProducto, double precioProducto, String descripcionProducto, int cantidad, int puntosProducto);
}