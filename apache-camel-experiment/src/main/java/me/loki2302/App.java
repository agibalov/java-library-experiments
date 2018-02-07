package me.loki2302;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.guice.CamelModuleWithRouteTypes;

public class App {
    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(
                new CamelModuleWithRouteTypes(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(Consumer.class).asEagerSingleton();
                    }
                });

        CamelContext camelContext = injector.getInstance(CamelContext.class);

        Producer producer = injector.getInstance(Producer.class);
        producer.produce("hi there");

        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.setDefaultEndpointUri("direct:test");
        producerTemplate.sendBody("wtf");
    }

    public static class Producer {
        @Produce(uri = "direct:test")
        private ProducerTemplate producerTemplate;

        public void produce(String data) {
            producerTemplate.sendBody(data);
        }
    }

    public static class Consumer {
        @Consume(uri = "direct:test")
        public void consume(String data) {
            System.out.printf("Consumed: %s\n", data);
        }
    }
}
