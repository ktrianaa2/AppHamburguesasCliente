package com.example.apphamburguesascliente.Modelos;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ProductoModelo {
    private int idProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private double precioProducto;
    private int puntosProducto;

    public static List<ProductoModelo> fromJsonArray(JsonArray productosArray) {
        List<ProductoModelo> listaProductos = new ArrayList<>();

        try {
            for (int i = 0; i < productosArray.size(); i++) {
                JsonObject jsonProducto = productosArray.get(i).getAsJsonObject();

                ProductoModelo producto = new ProductoModelo();
                producto.setIdProducto(jsonProducto.get("id_producto").getAsInt());
                producto.setNombreProducto(jsonProducto.get("nombreproducto").getAsString());
                producto.setDescripcionProducto(jsonProducto.get("descripcionproducto").getAsString());
                producto.setPrecioProducto(jsonProducto.get("preciounitario").getAsDouble());
                producto.setPuntosProducto(jsonProducto.has("puntosp") ? jsonProducto.get("puntosp").getAsInt() : 0);

                listaProductos.add(producto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProductos;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public int getPuntosProducto() {
        return puntosProducto;
    }

    public void setPuntosProducto(int puntosProducto) {
        this.puntosProducto = puntosProducto;
    }


}
