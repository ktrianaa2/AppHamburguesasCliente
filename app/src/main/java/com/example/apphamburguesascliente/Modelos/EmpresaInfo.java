package com.example.apphamburguesascliente.Modelos;

import com.google.gson.annotations.SerializedName;

public class EmpresaInfo {
    @SerializedName("enombre")
    private String nombre;
    @SerializedName("direccion")
    private String direccion;
    @SerializedName("etelefono")
    private String telefono;
    @SerializedName("correoelectronico")
    private String correoElectronico;
    @SerializedName("fechafundacion")
    private String fechaFundacion;
    @SerializedName("sitioweb")
    private String sitioWeb;
    @SerializedName("eslogan")
    private String eslogan;

    @SerializedName("elogo")
    private String logoBase64;


    // Getters
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getCorreoElectronico() { return correoElectronico; }
    public String getFechaFundacion() { return fechaFundacion; }
    public String getSitioWeb() { return sitioWeb; }
    public String getEslogan() { return eslogan; }

    public String getLogoBase64() {
        return logoBase64;
    }

    @Override
    public String toString() {
        return "EmpresaInfo{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", fechaFundacion='" + fechaFundacion + '\'' +
                ", sitioWeb='" + sitioWeb + '\'' +
                ", eslogan='" + eslogan + '\'' +
                '}';
    }
}