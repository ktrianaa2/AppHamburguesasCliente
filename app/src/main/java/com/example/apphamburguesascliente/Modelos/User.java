package com.example.apphamburguesascliente.Modelos;

public class User {
    private String nombre_usuario;
    private int idcliente;
    private String telefono;
    private String razon_social;
    private String tipo_cliente;
    private String snombre;
    private String capellido;
    private String ruc_cedula;

    private String cpuntos;
    private Ubicacion ubicacion1;
    private Ubicacion ubicacion2;
    private Ubicacion ubicacion3;

    public String getNombreUsuario() {
        return nombre_usuario;
    }

    public void setNombreUsuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public int getIdCliente() {
        return idcliente;
    }

    public void setIdCliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRazonSocial() {
        return razon_social;
    }

    public void setRazonSocial(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getTipoCliente() {
        return tipo_cliente;
    }

    public void setTipoCliente(String tipo_cliente) {
        this.tipo_cliente = tipo_cliente;
    }

    public String getSnombre() {
        return snombre;
    }

    public void setSnombre(String snombre) {
        this.snombre = snombre;
    }

    public String getCapellido() {
        return capellido;
    }

    public void setCapellido(String capellido) {
        this.capellido = capellido;
    }

    public String getRucCedula() {
        return ruc_cedula;
    }

    public void setRucCedula(String ruc_cedula) {
        this.ruc_cedula = ruc_cedula;
    }
    public String getCpuntos() {
        return cpuntos;
    }

    public void setCpuntos(String cpuntos) {
        this.cpuntos = cpuntos;
    }



    public Ubicacion getUbicacion1() {
        return ubicacion1;
    }

    public void setUbicacion1(Ubicacion ubicacion1) {
        this.ubicacion1 = ubicacion1;
    }

    public Ubicacion getUbicacion2() {
        return ubicacion2;
    }

    public void setUbicacion2(Ubicacion ubicacion2) {
        this.ubicacion2 = ubicacion2;
    }

    public Ubicacion getUbicacion3() {
        return ubicacion3;
    }

    public void setUbicacion3(Ubicacion ubicacion3) {
        this.ubicacion3 = ubicacion3;
    }
}
