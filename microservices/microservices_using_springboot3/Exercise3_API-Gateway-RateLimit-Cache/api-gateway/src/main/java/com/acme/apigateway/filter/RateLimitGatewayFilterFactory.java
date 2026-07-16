package com.acme.apigateway.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple in-memory, per-client-IP token-bucket rate limiter, built as
 * a proper Spring Cloud Gateway filter factory so it can be attached to
 * individual routes and configured per-route (see application.yml).
 * <p>
 * Spring Cloud Gateway's built-in RequestRateLimiter filter requires a
 * Redis-backed implementation out of the box; this exists so the whole
 * exercise runs without needing to stand up Redis. For a real multi-instance
 * gateway deployment you'd want a shared store (Redis, etc.) instead of
 * per-instance in-memory buckets like this one.
 * <p>
 * Referenced in application.yml simply as "RateLimit" (Spring strips the
 * "GatewayFilterFactory" suffix when matching filter names).
 */
@Component
public class RateLimitGatewayFilterFactory extends AbstractGatewayFilterFactory<RateLimitGatewayFilterFactory.Config> {

    private final Map<String, Bucket> bucketsByClient = new ConcurrentHashMap<>();

    public RateLimitGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String clientKey = resolveClientKey(exchange);
            Bucket bucket = bucketsByClient.computeIfAbsent(clientKey, key -> newBucket(config));

            if (bucket.tryConsume(1)) {
                return chain.filter(exchange);
            }

            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().add("Retry-After", String.valueOf(config.getRefillPeriodSeconds()));
            return exchange.getResponse().setComplete();
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("capacity", "refillTokens", "refillPeriodSeconds");
    }

    private Bucket newBucket(Config config) {
        Bandwidth limit = Bandwidth.classic(config.getCapacity(),
                Refill.greedy(config.getRefillTokens(), Duration.ofSeconds(config.getRefillPeriodSeconds())));
        return Bucket.builder().addLimit(limit).build();
    }

    private String resolveClientKey(ServerWebExchange exchange) {
        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        return (remoteAddress != null && remoteAddress.getAddress() != null)
                ? remoteAddress.getAddress().getHostAddress()
                : "unknown-client";
    }

    public static class Config {
        private int capacity = 5;
        private int refillTokens = 5;
        private int refillPeriodSeconds = 10;

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getRefillTokens() {
            return refillTokens;
        }

        public void setRefillTokens(int refillTokens) {
            this.refillTokens = refillTokens;
        }

        public int getRefillPeriodSeconds() {
            return refillPeriodSeconds;
        }

        public void setRefillPeriodSeconds(int refillPeriodSeconds) {
            this.refillPeriodSeconds = refillPeriodSeconds;
        }
    }
}
