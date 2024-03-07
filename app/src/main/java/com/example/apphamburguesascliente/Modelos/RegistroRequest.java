package com.example.apphamburguesascliente.Modelos;

public class RegistroRequest {
    private String nombreusuario;
    private String contrasenia;
    private String ctelefono;
    private String snombre;
    private String capellido;

    public RegistroRequest(String nombreusuario, String contrasenia, String ctelefono, String snombre, String capellido) {
        this.nombreusuario = nombreusuario;
        this.contrasenia = contrasenia;
        this.ctelefono = ctelefono;
        this.snombre = snombre;
        this.capellido = capellido;
    }
}