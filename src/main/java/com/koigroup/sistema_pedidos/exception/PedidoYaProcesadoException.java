package com.koigroup.sistema_pedidos.exception;

public class PedidoYaProcesadoException extends PedidoException {

    public PedidoYaProcesadoException() {
        super("El pedido ya fue procesado");
    }
}
