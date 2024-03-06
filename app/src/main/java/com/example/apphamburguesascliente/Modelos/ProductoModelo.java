package com.example.apphamburguesascliente.Modelos;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ProductoModelo {
    private int idProducto;
    private String nombreProducto;

    public static List<ProductoModelo> fromJsonArray(JsonArray productosArray) {
        List<ProductoModelo> listaProductos = new ArrayList<>();

        try {
            for (int i = 0; i < productosArray.size(); i++) {
                JsonObject jsonProducto = productosArray.get(i).getAsJsonObject();

                ProductoModelo producto = new ProductoModelo();
                producto.setIdProducto(jsonProducto.get("id_producto").getAsInt());
                producto.setNombreProducto(jsonProducto.get("nombreproducto").getAsString());

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
}
