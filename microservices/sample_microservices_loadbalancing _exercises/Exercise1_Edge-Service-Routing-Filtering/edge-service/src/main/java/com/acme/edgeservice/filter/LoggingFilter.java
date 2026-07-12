package com.acme.edgeservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

/**
 * A global filter that logs every request that passes through the
 * gateway, along with how long it took the downstream service to
 * respond. Implementing {@link Ordered} lets us control where in the
 * filter chain this runs relative to other global filters.
 */
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Instant start = Instant.now();
        String method = exchange.getRequest().getMethod() != null
                ? exchange.getRequest().getMethod().name()
                : "UNKNOWN";
        String path = exchange.getRequest().getURI().toString();

        log.info("Incoming request -> {} {}", method, path);

        return chain.filter(exchange).doFinally(signalType -> {
            Duration elapsed = Duration.between(start, Instant.now());
            int statusCode = exchange.getResponse().getStatusCode() != null
                    ? exchange.getResponse().getStatusCode().value()
                    : 0;
            log.info("Completed request -> {} {} [{}] in {} ms",
                    method, path, statusCode, elapsed.toMillis());
        });
    }

    @Override
    public int getOrder() {
        // Run early so we capture the request before other filters mutate it.
        return -1;
    }
}
