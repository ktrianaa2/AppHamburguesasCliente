package com.example.apphamburguesascliente.Modelos;

import com.google.gson.annotations.SerializedName;

public class RespuestaEmpresa {
    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("empresa_info")
    private EmpresaInfo empresaInfo;

    // Getters y setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public EmpresaInfo getEmpresaInfo() {
        return empresaInfo;
    }

    public void setEmpresaInfo(EmpresaInfo empresaInfo) {
        this.empresaInfo = empresaInfo;
    }
}
