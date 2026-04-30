package com.koigroup.sistema_pedidos.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.koigroup.sistema_pedidos.entities.Carrito;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarritoDTO {
    private Long id;
    private String codigo;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    public static CarritoDTO from(Carrito carrito) {
        return new CarritoDTO(
                carrito.getId(),
                carrito.getCodigo(),
                carrito.getUsuario().getId()
        );
    }
}
