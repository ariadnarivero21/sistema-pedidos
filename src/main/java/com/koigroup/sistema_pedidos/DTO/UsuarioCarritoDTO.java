package com.koigroup.sistema_pedidos.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioCarritoDTO {

    private Long id;

    @JsonProperty("usuario_id")
    private Long usuarioId;
    private String codigo;

}
