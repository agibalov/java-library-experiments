package me.loki2302;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    private final static String BROKER_URL = "tcp://localhost:2302";
    private BrokerService broker;
    private Connection connection;

    @Before
    public void startBrokerAndStartConnection() throws Exception {
        broker = new BrokerService();
        broker.addConnector(BROKER_URL);
        broker.start();

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = connectionFactory.createConnection();
        connection.start();
    }

    @After
    public void closeConnectionAndStopBroker() throws Exception {
        connection.close();
        connection = null;

        broker.stop();
        broker = null;
    }

    @Test
    public void dummy() throws JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic testTopic = session.createTopic("test");

        MessageProducer producer = session.createProducer(testTopic);
        MessageConsumer consumer = session.createConsumer(testTopic);

        producer.send(session.createTextMessage("hello"));

        Message message = consumer.receive();
        String text = ((TextMessage)message).getText();
        assertEquals("hello", text);
    }
}
