package com.example.apphamburguesascliente.Modelos;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ComboModelo {
    private List<Combo> combos;

    public List<Combo> getCombos() {
        return combos;
    }

    public void setCombos(List<Combo> combos) {
        this.combos = combos;
    }


    public static List<Combo> fromJsonArray(JsonArray jsonArray) {
        List<Combo> combos = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject comboJson = jsonElement.getAsJsonObject();
            Combo combo = new Combo();

            // Comprobación de nulidad antes de acceder a los valores
            if (comboJson != null) {
                if (comboJson.has("idCombo") && !comboJson.get("idCombo").isJsonNull()) {
                    combo.setIdCombo(comboJson.get("idCombo").getAsInt());
                } else {
                    combo.setIdCombo(0); // Puedes asignar un valor predeterminado o tomar alguna otra acción apropiada
                }

                combo.setNombreCb(comboJson.has("nombreCb") ? comboJson.get("nombreCb").getAsString() : "");
                combo.setDescripcionCombo(comboJson.has("descripcionCombo") ? comboJson.get("descripcionCombo").getAsString() : "");
                combo.setPrecioUnitario(comboJson.has("precioUnitario") ? comboJson.get("precioUnitario").getAsString() : "");
               // combo.setImagen(comboJson.has("imagen") ? comboJson.get("imagen").getAsString() : "");
                combo.setPuntos(comboJson.has("puntos") ? comboJson.get("puntos").getAsString() : "");
                combo.setIva(comboJson.has("iva") ? comboJson.get("iva").getAsString() : "");

                // Populate list of Producto objects
                if (comboJson.has("productos") && comboJson.get("productos").isJsonArray()) {
                    JsonArray productosArray = comboJson.getAsJsonArray("productos");
                    List<Producto> productos = new ArrayList<>();

                    for (JsonElement productoElement : productosArray) {
                        JsonObject productoJson = productoElement.getAsJsonObject();
                        Producto producto = new Producto();

                        // Populate Producto object from JSON
                        producto.setIdProducto(productoJson.has("idProducto") ? productoJson.get("idProducto").getAsInt() : 0);
                        producto.setNombreProducto(productoJson.has("nombreProducto") ? productoJson.get("nombreProducto").getAsString() : "");
                        producto.setDescripcionProducto(productoJson.has("descripcionProducto") ? productoJson.get("descripcionProducto").getAsString() : "");
                        producto.setPrecioUnitario(productoJson.has("precioUnitario") ? productoJson.get("precioUnitario").getAsString() : "");
                        producto.setIva(productoJson.has("iva") ? productoJson.get("iva").getAsString() : "");
                        producto.setCantidad(productoJson.has("cantidad") ? productoJson.get("cantidad").getAsInt() : 0);

                        productos.add(producto);
                    }

                    combo.setProductos(productos);
                }

                combos.add(combo);
            }
        }

        return combos;
    }

    public static class Combo {
        private int idCombo;
        private String nombreCb;
        private String descripcionCombo;
        private String precioUnitario;
        private String imagen;
        private String puntos;
        private String iva;
        private List<Producto> productos;

        public int getIdCombo() {
            return idCombo;
        }

        public void setIdCombo(int idCombo) {
            this.idCombo = idCombo;
        }

        public String getNombreCb() {
            return nombreCb;
        }

        public void setNombreCb(String nombreCb) {
            this.nombreCb = nombreCb;
        }

        public String getDescripcionCombo() {
            return descripcionCombo;
        }

        public void setDescripcionCombo(String descripcionCombo) {
            this.descripcionCombo = descripcionCombo;
        }

        public String getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(String precioUnitario) {
            this.precioUnitario = precioUnitario;
        }

        public String getImagen() {
            return imagen;
        }

        public void setImagen(String imagen) {
            this.imagen = imagen;
        }

        public String getPuntos() {
            return puntos;
        }

        public void setPuntos(String puntos) {
            this.puntos = puntos;
        }

        public String getIva() {
            return iva;
        }

        public void setIva(String iva) {
            this.iva = iva;
        }


        public List<Producto> getProductos() {
            return productos;
        }

        public void setProductos(List<Producto> productos) {
            this.productos = productos;
        }
    }

    public static class Producto {
        private int idProducto;
        private String nombreProducto;
        private String descripcionProducto;
        private String precioUnitario;
        private String iva;
        private int cantidad;

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

        public String getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(String precioUnitario) {
            this.precioUnitario = precioUnitario;
        }

        public String getIva() {
            return iva;
        }

        public void setIva(String iva) {
            this.iva = iva;
        }


        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }
    }
}
