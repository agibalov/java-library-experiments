package me.loki2302;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.camel.*;
import org.apache.camel.guice.CamelModuleWithRouteTypes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DummyProducerConsumerTest {
    @Test
    public void canProduceWithInjectedProducerTemplate() {
        Injector injector = Guice.createInjector(
                new CamelModuleWithRouteTypes(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DummyProducer.class).asEagerSingleton();
                        bind(DummyConsumer.class).asEagerSingleton();
                    }
                });

        DummyProducer dummyProducer = injector.getInstance(DummyProducer.class);
        dummyProducer.produce("hi there");

        DummyConsumer dummyConsumer = injector.getInstance(DummyConsumer.class);
        assertEquals(1, dummyConsumer.consumptionCount);
    }

    @Test
    public void canProduceWithProducerTemplateRetrievedManually() {
        Injector injector = Guice.createInjector(
                new CamelModuleWithRouteTypes(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DummyConsumer.class).asEagerSingleton();
                    }
                });

        CamelContext camelContext = injector.getInstance(CamelContext.class);

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.setDefaultEndpointUri("direct:test");
        producerTemplate.sendBody("hi there");

        DummyConsumer dummyConsumer = injector.getInstance(DummyConsumer.class);
        assertEquals(1, dummyConsumer.consumptionCount);
    }

    public static class DummyProducer {
        @Produce(uri = "direct:test")
        private ProducerTemplate producerTemplate;

        public void produce(String data) {
            producerTemplate.sendBody(data);
        }
    }

    public static class DummyConsumer {
        public int consumptionCount;

        @Consume(uri = "direct:test")
        public void consume(String data) {
            ++consumptionCount;
        }
    }
}
