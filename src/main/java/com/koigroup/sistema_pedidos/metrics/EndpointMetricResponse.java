package com.koigroup.sistema_pedidos.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EndpointMetricResponse {

    private String endpoint;

    @JsonProperty("total_llamados")
    private long totalLlamados;

    private long exitosos;
    private long fallidos;

    @JsonProperty("tiempo_total_ms")
    private long tiempoTotalMs;

    @JsonProperty("tiempo_promedio_ms")
    private double tiempoPromedioMs;
}
