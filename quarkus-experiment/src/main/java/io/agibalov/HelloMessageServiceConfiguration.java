package io.agibalov;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class HelloMessageServiceConfiguration {
    @Produces
    public HelloMessageService helloMessageService(
            @ConfigProperty(name = "app.hello-message-suffix") String helloMessageSuffix) {

        return new HelloMessageService(helloMessageSuffix);
    }
}
