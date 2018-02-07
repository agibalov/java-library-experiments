package me.loki2302;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;

public class App {
    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:2302");
        broker.setPersistent(false);
        broker.start();
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost:2302");
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic testTopic = session.createTopic("test");

            MessageProducer producer = session.createProducer(testTopic);
            MessageConsumer consumer = session.createConsumer(testTopic);

            producer.send(session.createTextMessage("hello"));

            Message message = consumer.receive();
            System.out.println(((TextMessage) message).getText());

            connection.close();
        } finally {
            broker.stop();
        }

        System.out.println("done");
    }
}
