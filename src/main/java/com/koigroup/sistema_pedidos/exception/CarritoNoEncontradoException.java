package com.koigroup.sistema_pedidos.exception;

public class CarritoNoEncontradoException extends PedidoException {

    public CarritoNoEncontradoException(String codigoCarrito) {
        super("No se encontró un carrito con el código: " + codigoCarrito);
    }
}
