package com.koigroup.sistema_pedidos.metrics;

import java.util.concurrent.atomic.AtomicLong;

public class EndpointMetric {

    private final AtomicLong totalLlamados = new AtomicLong(0);
    private final AtomicLong exitosos = new AtomicLong(0);
    private final AtomicLong fallidos = new AtomicLong(0);
    private final AtomicLong tiempoTotalMs = new AtomicLong(0);

    public void registerLlamado(long duracionMs, int status) {
        totalLlamados.incrementAndGet();
        tiempoTotalMs.addAndGet(duracionMs);

        if (status >= 200 && status < 300) {
            exitosos.incrementAndGet();
        } else {
            fallidos.incrementAndGet();
        }
    }

    public long getTotalLlamados() {
        return totalLlamados.get();
    }

    public long getExitosos() {
        return exitosos.get();
    }

    public long getFallidos() {
        return fallidos.get();
    }

    public long getTiempoTotalMs() {
        return tiempoTotalMs.get();
    }

    public double getTiempoPromedioMs() {
        long total = totalLlamados.get();
        return total == 0 ? 0.0 : (double) tiempoTotalMs.get() / total;
    }
}
