package com.koigroup.sistema_pedidos.exception;

public class CarritoVacioException extends PedidoException {

    public CarritoVacioException() {
        super("El carrito no tiene ningún producto");
    }
}
