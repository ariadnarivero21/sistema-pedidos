package com.koigroup.sistema_pedidos.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NuevoProductoRequest {

    @NotBlank(message = "El código del carrito es obligatorio")
    @JsonProperty("codigo_carrito")
    private String codigoCarrito;

    @NotBlank(message = "El código del producto es obligatorio")
    @JsonProperty("codigo_producto")
    private String codigoProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @JsonProperty(value = "cantidad_producto", defaultValue = "1")
    private Integer cantidadProducto;
}
