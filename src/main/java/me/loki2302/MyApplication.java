package me.loki2302;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class MyApplication extends Application {
    @Override
    public Set<Object> getSingletons() {
        Set<Object> services = new HashSet<Object>();
        
        services.add(new CalculatorApiImpl());
        services.add(new PersonResource());
        
        return services;
    } 
}