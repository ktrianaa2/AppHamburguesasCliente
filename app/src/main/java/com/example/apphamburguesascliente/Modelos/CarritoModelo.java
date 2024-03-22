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
        // Verificar si el producto ya está en el carrito utilizando la clave
        for (Producto p : productos) {
            if (p.getClave().equals(producto.getClave())) {
                // El producto ya está en el carrito, actualizar la cantidad utilizando el método actualizarCantidadProducto
                actualizarCantidadProducto(p.getClave(), p.getCantidad() + producto.getCantidad());
                return; // Salir del método
            }
        }
        // Si no se encontró el producto en el carrito, agregarlo
        productos.add(producto);
    }


    public void actualizarCantidadProducto(String claveProducto, int nuevaCantidad) {
        // Recorrer la lista de productos en el carrito
        for (Producto producto : productos) {
            // Verificar si la clave del producto coincide
            if (producto.getClave().equals(claveProducto)) {
                // Actualizar la cantidad del producto
                producto.setCantidad(producto.getCantidad() + nuevaCantidad);
                return; // Salir del método después de actualizar la cantidad
            }
        }
        // Si no se encontró ningún producto con la clave dada, no hacer nada
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
        private int puntos;

        private String clave;

        public Producto(String nombre, int id, double precio, int cantidad, int puntos, String clave) {
            this.nombre = nombre;
            this.id = id;
            this.precio = precio;
            this.cantidad = cantidad;
            this.puntos = puntos;
            this.clave = clave;
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

        public int getPuntos() {
            return puntos;
        }

        public void setPuntos(int puntos) {
            this.puntos = puntos;
        }

        public String getClave() {
            return clave;
        }

        public void setClave(String clave) {
            this.clave = clave;
        }
    }
}