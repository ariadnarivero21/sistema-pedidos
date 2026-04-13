package com.koigroup.sistema_pedidos.exception;

public class CarritoItemNoEncontradoException extends PedidoException {

    public CarritoItemNoEncontradoException(String codigoCarrito, String codigoProducto) {
        super("No se pudo encontrar el carritoItem asociado al codigoCarrito: " + codigoCarrito + " y al codigoProducto: " + codigoProducto);
    }
}
