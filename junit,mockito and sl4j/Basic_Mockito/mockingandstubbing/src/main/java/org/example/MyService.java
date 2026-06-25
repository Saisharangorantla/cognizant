package org.example;

/**
 * Simple service that depends on ExternalApi.
 */
public class MyService {
    private final ExternalApi externalApi;

    public MyService(ExternalApi externalApi) {
        this.externalApi = externalApi;
    }

    public String fetchData() {
        // In real code there might be transformation/validation; keep simple for the exercise
        return externalApi.getData();
    }
}

