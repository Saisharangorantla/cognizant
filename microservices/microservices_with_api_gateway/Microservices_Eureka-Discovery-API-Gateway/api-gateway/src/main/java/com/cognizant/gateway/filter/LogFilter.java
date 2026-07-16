package com.cognizant.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Matches the exercise sheet's LogFilter: a GlobalFilter runs on every
 * route the gateway handles, so this logs the URL of every request that
 * comes through - whether it's headed for account-service,
 * loan-service, or greet-service.
 */
@Component
public class LogFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("====>Request URL {}", exchange.getRequest().getURI());
        return chain.filter(exchange);
    }
}
