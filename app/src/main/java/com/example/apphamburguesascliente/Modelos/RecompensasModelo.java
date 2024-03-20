package com.example.apphamburguesascliente.Modelos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecompensasModelo {

    @SerializedName("recompensas_productos")
    private List<RecompensaProducto> recompensasProductos;

    public List<RecompensaProducto> getRecompensasProductos() {
        return recompensasProductos;
    }

    public static class RecompensaProducto {
        @SerializedName("id_recompensa_producto")
        private int idRecompensaProducto;

        @SerializedName("id_producto")
        private int idProducto;

        @SerializedName("puntos_recompensa_producto")
        private String puntosRecompensaProducto;

        public int getIdRecompensaProducto() {
            return idRecompensaProducto;
        }

        public int getIdProducto() {
            return idProducto;
        }

        public String getPuntosRecompensaProducto() {
            return puntosRecompensaProducto;
        }
    }
}
