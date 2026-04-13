package com.koigroup.sistema_pedidos.exception;

public class UsuarioNoEncontradoException extends PedidoException {

    public UsuarioNoEncontradoException(Long usuarioId) {
        super("No se pudo encontrar el usuario con ID: " + usuarioId);
    }
}
