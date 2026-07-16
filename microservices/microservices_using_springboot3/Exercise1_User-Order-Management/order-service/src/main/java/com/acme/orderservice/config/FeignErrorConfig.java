package com.acme.orderservice.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Translates a 404 from user-service into a clear "no such user" error
 * on this side, instead of letting Feign's generic FeignException bubble
 * up and produce an opaque 500.
 */
@Configuration
public class FeignErrorConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            if (response.status() == 404) {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Referenced user does not exist");
            }
            return new ErrorDecoder.Default().decode(methodKey, response);
        };
    }
}
