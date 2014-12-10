package me.loki2302;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.camel.Consume;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.guice.CamelModuleWithRouteTypes;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RoutingTest {
    @Test
    public void canRouteRequests() {
        Injector injector = Guice.createInjector(
                new CamelModuleWithRouteTypes(CalculatorRouteBuilder.class),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(CalculatorFacade.class).asEagerSingleton();
                        bind(CalculatorImplementation.class).asEagerSingleton();
                    }
                });

        CalculatorFacade calculatorFacade = injector.getInstance(CalculatorFacade.class);
        assertEquals(5, calculatorFacade.addNumbers(2, 3));
        assertEquals(-1, calculatorFacade.subNumbers(2, 3));
    }

    public static class CalculatorRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("direct:calculator").choice()
                    .when(header("action").isEqualTo("add")).to("direct:add")
                    .when(header("action").isEqualTo("sub")).to("direct:sub");
        }
    }

    public static class CalculatorFacade {
        @Produce
        private ProducerTemplate producerTemplate;

        public int addNumbers(int a, int b) {
            TwoIntegers twoIntegers = new TwoIntegers();
            twoIntegers.a = a;
            twoIntegers.b = b;

            Map<String, Object> headers = Collections.<String, Object>singletonMap("action", "add");

            Object result = producerTemplate.sendBodyAndHeaders(
                    "direct:calculator",
                    ExchangePattern.InOut,
                    twoIntegers,
                    headers);

            return (Integer)result;
        }

        public int subNumbers(int a, int b) {
            TwoIntegers twoIntegers = new TwoIntegers();
            twoIntegers.a = a;
            twoIntegers.b = b;

            Map<String, Object> headers = Collections.<String, Object>singletonMap("action", "sub");

            Object result = producerTemplate.sendBodyAndHeaders(
                    "direct:calculator",
                    ExchangePattern.InOut,
                    twoIntegers,
                    headers);

            return (Integer)result;
        }
    }

    public static class CalculatorImplementation {
        @Consume(uri = "direct:add")
        public int addNumbers(TwoIntegers twoIntegers) {
            return twoIntegers.a + twoIntegers.b;
        }

        @Consume(uri = "direct:sub")
        public int subNumbers(TwoIntegers twoIntegers) {
            return twoIntegers.a - twoIntegers.b;
        }
    }

    public static class TwoIntegers {
        public int a;
        public int b;
    }
}
