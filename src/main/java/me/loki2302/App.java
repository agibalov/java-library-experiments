package me.loki2302;

import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class App {
    public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("win7dev-home");
        
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            
            Channel channel = null;
            try {
                channel = connection.createChannel();
                channel.queueDeclare("HelloQueue", false, false, false, null);
                channel.basicPublish("", "HelloQueue", null, "hello there!".getBytes());
                
                QueueingConsumer consumer = new QueueingConsumer(channel);
                channel.basicConsume("HelloQueue", true, consumer);
                
                Delivery delivery = consumer.nextDelivery();
                String m = new String(delivery.getBody());
                System.out.printf("Received: '%s'\n", m);
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
    }
}
