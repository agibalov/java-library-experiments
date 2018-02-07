package me.loki2302;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class CalculatorTest {
    @Test
    public void dummyTest() throws IOException {        
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(TestConfiguration.RabbitHostName);                
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(Calculator.CALCULATOR_RPC_QUEUE, false, false, false, null);
        channel.queuePurge(Calculator.CALCULATOR_RPC_QUEUE);
        channel.close();
        connection.close();
        
        CalculatorServer calculatorServer = CalculatorServer.make(TestConfiguration.RabbitHostName);
        CalculatorClient calculatorClient = CalculatorClient.make(TestConfiguration.RabbitHostName);        
        
        int result = calculatorClient.addNumbers(1, 2);
        assertEquals(3, result);
        calculatorClient.close();
        calculatorServer.close();        
    }
}
