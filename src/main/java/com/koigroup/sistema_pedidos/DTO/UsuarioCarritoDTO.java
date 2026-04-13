package com.koigroup.sistema_pedidos.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioCarritoDTO {

    private Long id;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    private String codigo;

}
