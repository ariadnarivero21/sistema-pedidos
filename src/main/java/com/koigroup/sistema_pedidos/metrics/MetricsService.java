package com.koigroup.sistema_pedidos.metrics;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    private final Map<String, EndpointMetric> metrics = new ConcurrentHashMap<>();

    public void register(String endpoint, long duracionMs, int status) {
        metrics.computeIfAbsent(endpoint, key -> new EndpointMetric())
                .registerLlamado(duracionMs, status);
    }

    public List<EndpointMetricResponse> getAll() {
        return metrics.entrySet()
                .stream()
                .map(entry -> {
                    String endpoint = entry.getKey();
                    EndpointMetric metric = entry.getValue();

                    return new EndpointMetricResponse(
                            endpoint,
                            metric.getTotalLlamados(),
                            metric.getExitosos(),
                            metric.getFallidos(),
                            metric.getTiempoTotalMs(),
                            metric.getTiempoPromedioMs()
                    );
                }).collect(Collectors.toList());
    }
}
