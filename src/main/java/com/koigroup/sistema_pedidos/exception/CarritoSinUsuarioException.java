package com.koigroup.sistema_pedidos.exception;

import org.springframework.http.HttpStatus;

public class CarritoSinUsuarioException extends PedidoException {

    public CarritoSinUsuarioException() {
        super("El carrito no tiene ningún usuario asociado", HttpStatus.BAD_REQUEST);
    }
}
