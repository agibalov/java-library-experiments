package me.loki2302;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DummyTest {
    private final static String BROKER_URL = "tcp://localhost:2302";
    private BrokerService broker;
    private Connection connection;

    @Before
    public void startBrokerAndStartConnection() throws Exception {
        broker = new BrokerService();
        broker.addConnector(BROKER_URL);
        broker.setPersistent(false);
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
    public void canUseTopic() throws JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic testTopic = session.createTopic("test");

        MessageProducer producer = session.createProducer(testTopic);
        MessageConsumer consumerA = session.createConsumer(testTopic);
        MessageConsumer consumerB = session.createConsumer(testTopic);

        producer.send(session.createTextMessage("hello"));

        Message messageA = consumerA.receive();
        String messageAText = ((TextMessage) messageA).getText();
        assertEquals("hello", messageAText);

        Message messageB = consumerB.receive();
        String messageBText = ((TextMessage) messageB).getText();
        assertEquals("hello", messageBText);
    }

    @Test
    public void canUseQueue() throws JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue testQueue = session.createQueue("test");

        MessageProducer producer = session.createProducer(testQueue);
        MessageConsumer consumerA = session.createConsumer(testQueue);

        producer.send(session.createTextMessage("hello1"));

        Message messageA = consumerA.receive();
        assertNotNull(messageA);
    }
}
