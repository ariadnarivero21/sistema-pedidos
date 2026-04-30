package com.koigroup.sistema_pedidos.exception;

import org.springframework.http.HttpStatus;

public class AccesoDenegadoException extends PedidoException {
    public AccesoDenegadoException(String username) {
        super("El usuario con username: " + username + " no tiene permiso para modificar este carrito."
                , HttpStatus.FORBIDDEN);
    }
}
