package com.koigroup.sistema_pedidos.exception;

import org.springframework.http.HttpStatus;

public class CarritoItemNoEncontradoException extends PedidoException {

    public CarritoItemNoEncontradoException(String codigoCarrito, String codigoProducto) {
        super("No se pudo encontrar el carritoItem asociado al codigoCarrito: " + codigoCarrito + " y al codigoProducto: "
                + codigoProducto, HttpStatus.NOT_FOUND);
    }
}
