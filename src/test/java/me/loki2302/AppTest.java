package me.loki2302;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class AppTest {
    @Test
    public void singleThreadTest() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
        final String queueName = "hello-queue";
        
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(TestConfiguration.RabbitMQHostName);
        
        boolean receivedMessage = false;
        
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            
            Channel channel = null;
            try {
                channel = connection.createChannel();
                channel.queueDeclare(queueName, false, false, false, null);
                channel.basicPublish("", queueName, null, "hello there!".getBytes());
                
                QueueingConsumer consumer = new QueueingConsumer(channel);                
                channel.basicConsume(queueName, true, consumer);
                
                Delivery delivery = consumer.nextDelivery();
                String m = new String(delivery.getBody());
                assertEquals("hello there!", m);
                receivedMessage = true;
            } finally {
                if(channel != null) {
                    channel.close();
                }
            }
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
        
        if(!receivedMessage) {
            fail();
        }
    }
}