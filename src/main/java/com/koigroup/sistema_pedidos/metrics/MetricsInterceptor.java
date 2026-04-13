package com.koigroup.sistema_pedidos.metrics;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Component
public class MetricsInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "START_TIME";

    private final MetricsService metricsService;

    public MetricsInterceptor(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                @Nullable Exception ex
    ) {
        Object startAttr = request.getAttribute(START_TIME);
        if (startAttr == null) {
            return;
        }

        long startTime = (long) startAttr;
        long duracionMs = System.currentTimeMillis() - startTime;

        Object patternAttr = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String pattern = patternAttr != null ? patternAttr.toString() : request.getRequestURI();
        String endpoint = request.getMethod() + " " + pattern;
        int status = response.getStatus();

        metricsService.register(endpoint, duracionMs, status);
    }
}
