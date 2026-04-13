package com.koigroup.sistema_pedidos.exception;

public class CarritoSinUsuarioException extends PedidoException {

    public CarritoSinUsuarioException() {
        super("El carrito no tiene ningún usuario asociado");
    }
}
