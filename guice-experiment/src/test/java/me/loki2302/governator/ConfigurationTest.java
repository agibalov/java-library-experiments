package me.loki2302.governator;

import com.google.inject.*;
import com.google.inject.name.Names;
import com.netflix.governator.annotations.Configuration;
import com.netflix.governator.configuration.PropertiesConfigurationProvider;
import com.netflix.governator.guice.BootstrapBinder;
import com.netflix.governator.guice.BootstrapModule;
import com.netflix.governator.guice.LifecycleInjector;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class ConfigurationTest {
    @Test
    public void canUseConfiguration() {
        Injector injector = LifecycleInjector.builder().withBootstrapModule(new BootstrapModule() {
            @Override
            public void configure(BootstrapBinder bootstrapBinder) {
                Properties properties = new Properties();
                properties.setProperty("app.text", "hello");
                properties.setProperty("app.number", "123");

                bootstrapBinder.bind(PropertiesConfigurationProvider.class)
                        .annotatedWith(Names.named("myConfigurationProvider"))
                        .toInstance(new PropertiesConfigurationProvider(properties));
                bootstrapBinder.bindConfigurationProvider()
                        .to(Key.get(PropertiesConfigurationProvider.class, Names.named("myConfigurationProvider")));
            }
        }).build().createInjector();

        Config config = injector.getInstance(Config.class);
        assertEquals("hello", config.text);
        assertEquals(123, config.number);
    }

    public static class Config {
        @Configuration("app.text")
        public String text;

        @Configuration("app.number")
        public int number;
    }
}
