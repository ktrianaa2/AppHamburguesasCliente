package com.example.apphamburguesascliente.Modelos;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

public class CarritoModelo implements Serializable {
    private static CarritoModelo instance;
    private List<Producto> productos;

    private CarritoModelo() {
        productos = new ArrayList<>();
    }

    public static synchronized CarritoModelo getInstance() {
        if (instance == null) {
            instance = new CarritoModelo();
        }
        return instance;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void limpiarCarrito() {
        productos.clear();
    }

    public static class Producto {
        private String nombre;
        private int id;
        private double precio;
        private int cantidad;

        public Producto(String nombre, int id, double precio, int cantidad) {
            this.nombre = nombre;
            this.id = id;
            this.precio = precio;
            this.cantidad = cantidad;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setPrecio(double precio) {
            this.precio = precio;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public String getNombre() {
            return nombre;
        }

        public int getId() {
            return id;
        }

        public double getPrecio() {
            return precio;
        }

        public int getCantidad() {
            return cantidad;
        }
    }
}
