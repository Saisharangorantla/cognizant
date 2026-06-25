package org.example;

public class Service {
    private final ExternalApi externalApi;
    public Service(ExternalApi externalApi){
        this.externalApi=externalApi;
    }
    public String getData(){
        return externalApi.getData();
    }
}
