package com.example.apphamburguesascliente.Modelos;

import java.util.ArrayList;
import java.util.List;

public class CarritoManager {
    private static List<ProductoModelo> carrito = new ArrayList<>();

    public static void añadirProducto(ProductoModelo producto) {
        carrito.add(producto);
    }

    public static List<ProductoModelo> obtenerCarrito() {
        return carrito;
    }

    public static void limpiarCarrito() {
        carrito.clear();
    }
}