package com.example.apphamburguesascliente.Modelos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AvisosModelo {

    @SerializedName("avisos_principales")
    private List<Aviso> avisosPrincipales;

    public List<Aviso> getAvisosPrincipales() {
        return avisosPrincipales;
    }

    public static class Aviso {
        private int id_aviso;
        private int id_empresa;
        private String titulo;
        private String descripcion;
        @SerializedName("imagen")
        private String imagenbase64;

        public int getId_aviso() {
            return id_aviso;
        }

        public void setId_aviso(int id_aviso) {
            this.id_aviso = id_aviso;
        }

        public int getId_empresa() {
            return id_empresa;
        }

        public void setId_empresa(int id_empresa) {
            this.id_empresa = id_empresa;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getImagen() {
            return imagenbase64;
        }

        public void setImagen(String imagen) {
            this.imagenbase64 = imagen;
        }
    }
}
