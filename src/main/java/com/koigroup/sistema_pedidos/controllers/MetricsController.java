package com.koigroup.sistema_pedidos.controllers;

import com.koigroup.sistema_pedidos.metrics.EndpointMetricResponse;
import com.koigroup.sistema_pedidos.metrics.MetricsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/metrics")
    public List<EndpointMetricResponse> getMetrics() {
        return metricsService.getAll();
    }
}
