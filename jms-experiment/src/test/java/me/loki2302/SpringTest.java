package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.Exchanger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTest.Config.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SpringTest {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Exchanger<String> exchanger;

    @Test
    public void canSendAndReceiveAMessage() throws InterruptedException {
        jmsTemplate.send("dummy-destination", session -> {
            return session.createTextMessage("hi there");
        });

        String message = exchanger.exchange(null);
        assertEquals("hi there", message);
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableJms
    public static class Config {
        @Bean
        public Exchanger<String> exchanger() {
            return new Exchanger<>();
        }

        @Bean
        public MessageReceiver messageReceiver() {
            return new MessageReceiver();
        }
    }

    public static class MessageReceiver {
        @Autowired
        private Exchanger<String> exchanger;

        @JmsListener(destination = "dummy-destination")
        public void receive(String messageBody) throws InterruptedException {
            exchanger.exchange(messageBody);
        }
    }
}
