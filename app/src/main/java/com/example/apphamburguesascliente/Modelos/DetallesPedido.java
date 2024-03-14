package com.example.apphamburguesascliente.Modelos;
import java.io.Serializable;
import java.util.List;

public class DetallesPedido implements Serializable {
    private List<DetalleProducto> detalles_pedido;

    public DetallesPedido(List<DetalleProducto> detalles) {
        this.detalles_pedido = detalles;
    }

    public List<DetalleProducto> getDetalles() {
        return detalles_pedido;
    }

    public void setDetalles(List<DetalleProducto> detalles) {
        this.detalles_pedido = detalles;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (DetalleProducto detalle : detalles_pedido) {
            stringBuilder.append("ID Producto: ").append(detalle.getIdProducto())
                    .append(", Cantidad: ").append(detalle.getCantidadPedido())
                    .append(", Costo Unitario: ").append(detalle.getCostoUnitario())
                    .append("\n");
        }
        return stringBuilder.toString();
    }

}