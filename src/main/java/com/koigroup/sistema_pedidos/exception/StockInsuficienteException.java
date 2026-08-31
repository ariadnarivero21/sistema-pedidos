package com.koigroup.sistema_pedidos.exception;

import org.springframework.http.HttpStatus;

public class StockInsuficienteException extends PedidoException {
    public StockInsuficienteException(Integer cantidad, String codigoProducto, Integer stockDisponible) {
        super("No hay stock de" + cantidad + " unidades para el producto con código: " + codigoProducto + ". Stock disponible: " + stockDisponible,
                HttpStatus.BAD_REQUEST);
    }
}
