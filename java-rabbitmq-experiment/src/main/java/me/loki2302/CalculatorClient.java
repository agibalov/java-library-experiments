package me.loki2302;

import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class CalculatorClient {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final Connection connection;
    private final Channel channel;
    
    private CalculatorClient(Connection connection, Channel channel) {
        this.connection = connection;
        this.channel = channel;
    }
    
    public int addNumbers(int a, int b) {
        try {
            String replyQueueName = channel.queueDeclare().getQueue();
            String requestCorrelationId = UUID.randomUUID().toString();
            BasicProperties requestProperties = new BasicProperties()
                .builder()
                .correlationId(requestCorrelationId)
                .replyTo(replyQueueName)
                .build();
            
            AddNumbersRequest addNumbersRequest = new AddNumbersRequest();
            addNumbersRequest.a = a;
            addNumbersRequest.b = b;
            byte[] requestBody = objectMapper.writeValueAsBytes(addNumbersRequest);            
            channel.basicPublish("", Calculator.CALCULATOR_RPC_QUEUE, requestProperties, requestBody);
            
            QueueingConsumer responseConsumer = new QueueingConsumer(channel);
            channel.basicConsume(replyQueueName, responseConsumer);
            Delivery responseDelivery = responseConsumer.nextDelivery();
            byte[] responseBody = responseDelivery.getBody();
            AddNumbersResponse response = objectMapper.readValue(responseBody, AddNumbersResponse.class);
            
            return response.result;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void close() {
        try {
            channel.close();
            connection.close();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static CalculatorClient make(String rabbitHostName) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(TestConfiguration.RabbitHostName);                
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            
            channel.queueDeclare(Calculator.CALCULATOR_RPC_QUEUE, false, false, false, null);
            return new CalculatorClient(connection, channel);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}