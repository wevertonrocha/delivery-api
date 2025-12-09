package com.deliverytech.delivery_api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements Filter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";

    private String generateCorrelationId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        try {
            // Obter ou gerar correlation ID
            String correlationId = httpRequest.getHeader(CORRELATION_ID_HEADER);
            if (correlationId == null || correlationId.trim().isEmpty()) {
                correlationId = generateCorrelationId();
            }

            // Adicionar ao MDC para logs
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

            // Adicionar informações extras ao MDC
            MDC.put("clientIp", getClientIpAddress(httpRequest));
            MDC.put("userAgent", httpRequest.getHeader("User-Agent"));
            MDC.put("sessionId", httpRequest.getSession().getId());
            MDC.put("requestUri", httpRequest.getRequestURI());
            MDC.put("httpMethod", httpRequest.getMethod());

            // Adicionar correlation ID ao header de resposta
            httpResponse.setHeader(CORRELATION_ID_HEADER, correlationId);

            // Continuar com a cadeia de filtros
            filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            // Limpar MDC após processamento
            MDC.clear();
        }
    }
}

