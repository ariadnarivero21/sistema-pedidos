package com.koigroup.sistema_pedidos.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductoCarritoDTO {

    private String nombre;
    private BigDecimal precio;
    private String codigo;
    private String categoria;
    private Integer cantidad;
}
