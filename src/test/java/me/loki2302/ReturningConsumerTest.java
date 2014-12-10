package me.loki2302;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.camel.Consume;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.guice.CamelModuleWithRouteTypes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReturningConsumerTest {
    @Test
    public void canReturnResultFromConsumer() {
        Injector injector = Guice.createInjector(
                new CamelModuleWithRouteTypes(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(CalculatorFacade.class).asEagerSingleton();
                        bind(CalculatorImplementation.class).asEagerSingleton();
                    }
                });

        CalculatorFacade calculatorFacade = injector.getInstance(CalculatorFacade.class);
        assertEquals(5, calculatorFacade.addNumbers(2, 3));
    }

    public static class CalculatorFacade {
        @Produce(uri = "direct:calculator")
        private ProducerTemplate producerTemplate;

        public int addNumbers(int a, int b) {
            TwoIntegers twoIntegers = new TwoIntegers();
            twoIntegers.a = a;
            twoIntegers.b = b;
            Object result = producerTemplate.sendBody(
                    "direct:calculator",
                    ExchangePattern.InOut,
                    twoIntegers);

            return (Integer)result;
        }
    }

    public static class CalculatorImplementation {
        @Consume(uri = "direct:calculator")
        public int addNumbers(TwoIntegers twoIntegers) {
            return twoIntegers.a + twoIntegers.b;
        }
    }

    public static class TwoIntegers {
        public int a;
        public int b;
    }
}
