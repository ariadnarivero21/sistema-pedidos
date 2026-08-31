package com.koigroup.sistema_pedidos.exception;

import org.springframework.http.HttpStatus;

public class ProductoNoEncontradoException extends PedidoException {

    public ProductoNoEncontradoException(String codigoProducto) {
        super("No se encontró el producto con código: " + codigoProducto, HttpStatus.NOT_FOUND);
    }
}
