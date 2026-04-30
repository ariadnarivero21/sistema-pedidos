package com.koigroup.sistema_pedidos.exception;

import org.springframework.http.HttpStatus;

public class UsuarioNoEncontradoException extends PedidoException {

    public UsuarioNoEncontradoException(Long usuarioId) {
        super("No se pudo encontrar el usuario con ID: " + usuarioId, HttpStatus.NOT_FOUND);
    }
}
