package com.koigroup.sistema_pedidos.exception;

import org.springframework.http.HttpStatus;

public class PedidoYaProcesadoException extends PedidoException {

    public PedidoYaProcesadoException() {
        super("El pedido ya fue procesado", HttpStatus.CONFLICT);
    }
}
