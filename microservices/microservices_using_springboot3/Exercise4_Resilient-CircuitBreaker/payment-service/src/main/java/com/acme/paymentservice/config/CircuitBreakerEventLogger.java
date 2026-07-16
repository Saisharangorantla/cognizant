package com.acme.paymentservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Subscribes to every circuit breaker's event stream and logs the
 * interesting bits (state transitions, calls rejected while open,
 * recorded errors) - this is the "log and monitor fallback events"
 * requirement. The same events are also available at runtime via
 * actuator: GET /actuator/circuitbreakerevents and
 * GET /actuator/circuitbreakers.
 */
@Component
public class CircuitBreakerEventLogger {

    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerEventLogger.class);

    public CircuitBreakerEventLogger(CircuitBreakerRegistry registry) {
        // Cover breakers that already exist...
        registry.getAllCircuitBreakers().forEach(this::registerListeners);
        // ...and any created later (Resilience4j creates them lazily on first use).
        registry.getEventPublisher().onEntryAdded(event -> registerListeners(event.getAddedEntry()));
    }

    private void registerListeners(CircuitBreaker circuitBreaker) {
        circuitBreaker.getEventPublisher()
                .onStateTransition(event -> log.warn("[{}] state transition: {} -> {}",
                        circuitBreaker.getName(),
                        event.getStateTransition().getFromState(),
                        event.getStateTransition().getToState()))
                .onError(event -> log.error("[{}] call failed after {} ms: {}",
                        circuitBreaker.getName(),
                        event.getElapsedDuration().toMillis(),
                        event.getThrowable().toString()))
                .onCallNotPermitted(event -> log.warn(
                        "[{}] call rejected - circuit is OPEN", circuitBreaker.getName()))
                .onFailureRateExceeded(event -> log.warn("[{}] failure rate exceeded: {}%",
                        circuitBreaker.getName(), event.getFailureRate()));
    }
}
