package me.loki2302;

import static org.junit.Assert.*;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class AppTest {    
    @Test
    public void defaultExchangeTest() {
        withChannel(new ChannelFunc() {
            @Override
            public void run(Channel channel) throws Exception {
                channel.queueDeclare("q", false, false, false, null);
                channel.basicPublish("", "q", null, "hello".getBytes());
                                
                QueueingConsumer consumer = new QueueingConsumer(channel);                
                channel.basicConsume("q", true, consumer);
                
                Delivery delivery = consumer.nextDelivery();
                String m = new String(delivery.getBody());
                assertEquals("hello", m);
            }
        }); 
    }
    
    @Test
    public void directExchangeTest() {
        withChannel(new ChannelFunc() {
            @Override
            public void run(Channel channel) throws Exception {
                channel.queueDeclare("q", false, false, false, null);
                channel.exchangeDeclare("ex", "direct");                
                channel.queueBind("q", "ex", "");
                channel.basicPublish("ex", "", null, "hello".getBytes());
                
                QueueingConsumer consumer = new QueueingConsumer(channel);
                channel.basicConsume("q", true, consumer);                
                Delivery delivery = consumer.nextDelivery();
                String m = new String(delivery.getBody());
                assertEquals("hello", m);
            }            
        });
    }
    
    @Test
    public void fanoutExchangeTest() {
        withChannel(new ChannelFunc() {
            @Override
            public void run(Channel channel) throws Exception {
                channel.queueDeclare("q1", false, false, false, null);
                channel.queueDeclare("q2", false, false, false, null);
                channel.exchangeDeclare("ex1", "fanout");
                channel.queueBind("q1", "ex1", "");
                channel.queueBind("q2", "ex1", "");
                channel.basicPublish("ex1", "", null, "hello".getBytes());
                
                QueueingConsumer consumer1 = new QueueingConsumer(channel);
                channel.basicConsume("q1", true, consumer1);                
                Delivery delivery1 = consumer1.nextDelivery();
                String m1 = new String(delivery1.getBody());
                assertEquals("hello", m1);
                
                QueueingConsumer consumer2 = new QueueingConsumer(channel);
                channel.basicConsume("q2", true, consumer2);                
                Delivery delivery2 = consumer2.nextDelivery();
                String m2 = new String(delivery2.getBody());
                assertEquals("hello", m2);
            }            
        });
    }
    
    @Test
    public void topicExchangeTest() {
        withChannel(new ChannelFunc() {
            @Override
            public void run(Channel channel) throws Exception {
                channel.queueDeclare("news", false, false, false, null);
                channel.queueDeclare("good-news", false, false, false, null);
                channel.queueDeclare("bad-news", false, false, false, null);
                channel.exchangeDeclare("ex2", "topic");
                channel.queueBind("news", "ex2", "news.*");
                channel.queueBind("good-news", "ex2", "news.good");
                channel.queueBind("bad-news", "ex2", "news.bad");
                
                channel.basicPublish("ex2", "news.good", null, "good news".getBytes());
                channel.basicPublish("ex2", "news.bad", null, "bad news".getBytes());
                
                QueueingConsumer newsConsumer = new QueueingConsumer(channel);
                channel.basicConsume("news", true, newsConsumer);                
                Delivery newsDelivery1 = newsConsumer.nextDelivery();
                String newsMessage1 = new String(newsDelivery1.getBody());
                assertEquals("good news", newsMessage1);
                Delivery newsDelivery2 = newsConsumer.nextDelivery();
                String newsMessage2 = new String(newsDelivery2.getBody());
                assertEquals("bad news", newsMessage2);
                
                QueueingConsumer goodNewsConsumer = new QueueingConsumer(channel);
                channel.basicConsume("good-news", true, goodNewsConsumer);
                Delivery goodNewsDelivery = goodNewsConsumer.nextDelivery();
                String goodNewsMessage = new String(goodNewsDelivery.getBody());
                assertEquals("good news", goodNewsMessage);
                
                QueueingConsumer badNewsConsumer = new QueueingConsumer(channel);
                channel.basicConsume("bad-news", true, badNewsConsumer);
                Delivery badNewsDelivery = badNewsConsumer.nextDelivery();
                String badNewsMessage = new String(badNewsDelivery.getBody());
                assertEquals("bad news", badNewsMessage);
            }            
        });
    }
    
    private static void withChannel(ChannelFunc func) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(TestConfiguration.RabbitMQHostName);
                    
            Connection connection = null;
            try {
                connection = connectionFactory.newConnection();
                
                Channel channel = null;
                try {
                    channel = connection.createChannel();
                    func.run(channel);
                } catch(Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    if(channel != null) {
                        channel.close();
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                if(connection != null) {
                    connection.close();
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static interface ChannelFunc {
        void run(Channel channel) throws Exception;
    }
}