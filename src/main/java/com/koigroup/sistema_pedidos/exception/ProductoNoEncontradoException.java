package com.koigroup.sistema_pedidos.exception;

public class ProductoNoEncontradoException extends PedidoException {

    public ProductoNoEncontradoException(String codigoProducto) {
        super("No se encontró un producto el código: " + codigoProducto);
    }
}
