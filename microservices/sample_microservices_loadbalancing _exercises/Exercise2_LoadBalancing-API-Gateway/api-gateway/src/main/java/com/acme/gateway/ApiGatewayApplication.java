package com.acme.gateway;

import com.acme.gateway.loadbalancer.CustomLoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;

/**
 * Gateway entry point. The {@link LoadBalancerClient} annotation wires our
 * custom load-balancing strategy (random selection instead of the default
 * round-robin) specifically for calls to the "example-service" client.
 */
@SpringBootApplication
@LoadBalancerClient(name = "example-service", configuration = CustomLoadBalancerConfiguration.class)
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
