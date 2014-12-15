package me.loki2302;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.camel.Consume;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.guice.CamelModuleWithRouteTypes;
import org.apache.camel.guice.jndi.JndiBind;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JmsTest {
    private final static String BROKER_URL = "tcp://localhost:2302";
    private BrokerService broker;

    @Before
    public void startBroker() throws Exception {
        broker = new BrokerService();
        broker.addConnector(BROKER_URL);
        broker.start();
    }

    @After
    public void stopBroker() throws Exception {
        broker.stop();
        broker = null;
    }

    @Test
    public void canUseJmsCalculator() throws JsonProcessingException {
        Injector injector = Guice.createInjector(
                new CamelModuleWithRouteTypes() {
                    @Provides
                    JmsComponent jms() {
                        return JmsComponent.jmsComponent(new ActiveMQConnectionFactory(BROKER_URL));
                    }
                },
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(CalculatorFacade.class).asEagerSingleton();
                        bind(CalculatorImplementation.class).asEagerSingleton();
                        bind(ObjectMapper.class);
                    }
                });

        CalculatorFacade calculatorFacade = injector.getInstance(CalculatorFacade.class);
        int result = calculatorFacade.addNumbers(2, 3);
        System.out.printf("GOT RESULT: %d\n", result);

        assertEquals(5, result);
    }

    public static class CalculatorFacade {
        @Inject
        private ObjectMapper objectMapper;

        @Produce
        private ProducerTemplate producerTemplate;

        public int addNumbers(int a, int b) throws JsonProcessingException {
            TwoIntegers twoIntegers = new TwoIntegers();
            twoIntegers.a = a;
            twoIntegers.b = b;
            String twoIntegersJson = objectMapper.writeValueAsString(twoIntegers);

            Object result = producerTemplate.sendBody(
                    "jms:queue:calculator",
                    ExchangePattern.InOut,
                    twoIntegersJson);

            return (Integer)result;
        }
    }

    public static class CalculatorImplementation {
        @Inject
        private ObjectMapper objectMapper;

        @Consume(uri = "jms:queue:calculator")
        public int addNumbers(String twoIntegersJson) throws IOException {
            TwoIntegers twoIntegers = objectMapper.readValue(twoIntegersJson, TwoIntegers.class);

            System.out.printf("GOT REQUEST: a=%d b=%d\n", twoIntegers.a, twoIntegers.b);
            return twoIntegers.a + twoIntegers.b;
        }
    }

    public static class TwoIntegers {
        public int a;
        public int b;
    }
}
