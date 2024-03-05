package com.example.apphamburguesascliente.Modelos;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductoModelo {
    private int idProducto;
    private int idCategoria;
    private int idUm;
    private String puntosp;
    private String codPrincipal;
    private String nombreProducto;
    private String descripcionProducto;
    private String precioUnitario;
    private String iva;
    private String ice;
    private String irbpnr;

    private String imagenp;

    public static List<ProductoModelo> fromJsonArray(JsonArray productosArray) {
        List<ProductoModelo> listaProductos = new ArrayList<>();

        try {
            for (int i = 0; i < productosArray.size(); i++) {
                JsonObject jsonProducto = productosArray.get(i).getAsJsonObject();

                ProductoModelo producto = new ProductoModelo();
                producto.setIdProducto(jsonProducto.get("idProducto").getAsInt());
                producto.setIdCategoria(jsonProducto.get("idCategoria").getAsInt());
                producto.setIdUm(jsonProducto.get("idUm").getAsInt());
                producto.setPuntosp(jsonProducto.get("puntosp").getAsString());
                producto.setCodPrincipal(jsonProducto.get("codPrincipal").getAsString());
                producto.setNombreProducto(jsonProducto.get("nombreProducto").getAsString());
                producto.setDescripcionProducto(jsonProducto.get("descripcionProducto").getAsString());
                producto.setPrecioUnitario(jsonProducto.get("precioUnitario").getAsString());
                producto.setIva(jsonProducto.get("iva").getAsString());
                producto.setIce(jsonProducto.get("ice").getAsString());
                producto.setIrbpnr(jsonProducto.get("irbpnr").getAsString());
                producto.setImagenp(jsonProducto.get("imagenp").getAsString());

                listaProductos.add(producto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProductos;
    }

    public String getImagenp() {
        return imagenp;
    }

    public void setImagenp(String imagenp) {
        this.imagenp = imagenp;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdUm() {
        return idUm;
    }

    public void setIdUm(int idUm) {
        this.idUm = idUm;
    }

    public String getPuntosp() {
        return puntosp;
    }

    public void setPuntosp(String puntosp) {
        this.puntosp = puntosp;
    }

    public String getCodPrincipal() {
        return codPrincipal;
    }

    public void setCodPrincipal(String codPrincipal) {
        this.codPrincipal = codPrincipal;
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

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public String getIrbpnr() {
        return irbpnr;
    }

    public void setIrbpnr(String irbpnr) {
        this.irbpnr = irbpnr;
    }
}