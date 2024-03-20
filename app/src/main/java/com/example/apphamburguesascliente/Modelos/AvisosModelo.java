package com.example.apphamburguesascliente.Modelos;

public class AvisosModelo {

    private int id_aviso;
    private int id_empresa;
    private String titulo;
    private String descripcion;
    private String imagen;

    // Constructor
    public AvisosModelo(int id_aviso, int id_empresa, String titulo, String descripcion, String imagen) {
        this.id_aviso = id_aviso;
        this.id_empresa = id_empresa;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    // Getters y setters
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
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
