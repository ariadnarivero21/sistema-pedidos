package com.koigroup.sistema_pedidos.exception;

import org.springframework.http.HttpStatus;

public class CarritoVacioException extends PedidoException {

    public CarritoVacioException() {
        super("El carrito no tiene ningún producto", HttpStatus.BAD_REQUEST);
    }
}
