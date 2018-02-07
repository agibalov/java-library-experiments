package me.loki2302;

import static org.junit.Assert.*;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CalculatorApiTest {
    private static NettyJaxrsServer netty;    
    
    @BeforeClass
    public static void startServer() {        
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setApplication(new MyApplication());
        
        netty = new NettyJaxrsServer();
        netty.setDeployment(deployment);
        netty.setPort(8080);
        netty.setRootResourcePath("/api");
        netty.setSecurityDomain(null);
        netty.start();       
    }
    
    @AfterClass
    public static void stopServer() {
        netty.stop();
        netty = null;
    }
    
    @Test
    public void canAddNumbers() {
        ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
        
        CalculatorApi calculatorApi = resteasyClient
                .target("http://localhost:8080/api/")
                .proxy(CalculatorApi.class);
        
        assertEquals(3, calculatorApi.addNumbers(1, 2));
    }
}