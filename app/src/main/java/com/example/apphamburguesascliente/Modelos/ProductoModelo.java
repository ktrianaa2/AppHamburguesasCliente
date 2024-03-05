package com.example.apphamburguesascliente.Modelos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public static ArrayList<ProductoModelo> fromJsonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<ProductoModelo> productos = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonProducto = jsonArray.getJSONObject(i);

            ProductoModelo producto = new ProductoModelo();
            producto.setIdProducto(jsonProducto.getInt("idProducto"));
            producto.setIdCategoria(jsonProducto.getInt("idCategoria"));
            producto.setIdUm(jsonProducto.getInt("idUm"));
            producto.setPuntosp(jsonProducto.getString("puntosp"));
            producto.setCodPrincipal(jsonProducto.getString("codPrincipal"));
            producto.setNombreProducto(jsonProducto.getString("nombreProducto"));
            producto.setDescripcionProducto(jsonProducto.getString("descripcionProducto"));
            producto.setPrecioUnitario(jsonProducto.getString("precioUnitario"));
            producto.setIva(jsonProducto.getString("iva"));
            producto.setIce(jsonProducto.getString("ice"));
            producto.setIrbpnr(jsonProducto.getString("irbpnr"));
            producto.setImagenp(jsonProducto.getString("imagenp"));

            productos.add(producto);
        }
        return productos;
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