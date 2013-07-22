package me.loki2302;

import org.codehaus.jackson.map.ObjectMapper;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class CalculatorServer {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final Connection connection;
    private final Channel channel;
    private Thread workerThread;
    
    private CalculatorServer(Connection connection, final Channel channel, final QueueingConsumer requestConsumer) {
        this.connection = connection;
        this.channel = channel;
        this.workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Delivery requestDelivery = requestConsumer.nextDelivery();                
                        CalculatorRequest calculatorRequest = objectMapper.readValue(
                                requestDelivery.getBody(), 
                                CalculatorRequest.class);
                        if(!(calculatorRequest instanceof AddNumbersRequest)) {
                            throw new RuntimeException("can't handle this message");
                        }
                        
                        AddNumbersRequest addNumbersRequest = (AddNumbersRequest)calculatorRequest;                
                        AddNumbersResponse addNumbersResponse = new AddNumbersResponse();
                        addNumbersResponse.result = addNumbersRequest.a + addNumbersRequest.b;
                        
                        byte[] responseBody = objectMapper.writeValueAsBytes(addNumbersResponse);
                        BasicProperties responseProperties = new BasicProperties()
                            .builder()
                            .correlationId(requestDelivery.getProperties().getCorrelationId())
                            .build();
                        channel.basicPublish("", requestDelivery.getProperties().getReplyTo(), responseProperties, responseBody);
                    } catch(InterruptedException e) {
                        break;
                    } catch(Exception e) {
                        throw new RuntimeException(e);
                    }
                }                
            }            
        });
        
        workerThread.start();
    }
    
    public void close() {
        try {
            workerThread.interrupt();
            workerThread.join();
            workerThread = null;
            
            channel.close();
            connection.close();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static CalculatorServer make(String rabbitHostName) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(TestConfiguration.RabbitHostName);                
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            
            channel.queueDeclare(Calculator.CALCULATOR_RPC_QUEUE, false, false, false, null);
            channel.queuePurge(Calculator.CALCULATOR_RPC_QUEUE);
            
            QueueingConsumer requestConsumer = new QueueingConsumer(channel);
            channel.basicConsume(Calculator.CALCULATOR_RPC_QUEUE, requestConsumer);            
            
            return new CalculatorServer(connection, channel, requestConsumer);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}