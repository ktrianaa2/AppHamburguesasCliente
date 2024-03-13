package com.example.apphamburguesascliente.Modelos;
import java.io.Serializable;
import java.util.List;

public class DetallesPedido implements Serializable {
    private List<DetalleProducto> detalles;

    public DetallesPedido(List<DetalleProducto> detalles) {
        this.detalles = detalles;
    }

    public List<DetalleProducto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleProducto> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (DetalleProducto detalle : detalles) {
            stringBuilder.append("ID Producto: ").append(detalle.getIdProducto())
                    .append(", Cantidad: ").append(detalle.getCantidadPedido())
                    .append(", Costo Unitario: ").append(detalle.getCostoUnitario())
                    .append("\n");
        }
        return stringBuilder.toString();
    }

}