package io.agibalov;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Property;
import jakarta.inject.Singleton;

@Factory
public class HelloMessageServiceFactory {
    @Singleton
    HelloMessageService helloMessageService(
            @Property(name = "app.hello-message-suffix") String helloMessageSuffix) {

        return new HelloMessageService(helloMessageSuffix);
    }
}
