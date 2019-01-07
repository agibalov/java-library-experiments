package me.loki2302;

import static org.junit.Assert.*;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.invm.InVMAcceptorFactory;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.junit.Test;

public class AppTest {
    @Test
    public void test() throws Exception {
        Configuration configuration = new ConfigurationImpl();
        configuration.setJournalDirectory("build/data/journal");
        configuration.setPersistenceEnabled(false);
        configuration.setSecurityEnabled(false);
        
        TransportConfiguration serverTransportConfiguration = new TransportConfiguration(InVMAcceptorFactory.class.getName());
        configuration.getAcceptorConfigurations().add(serverTransportConfiguration);
        HornetQServer server = HornetQServers.newHornetQServer(configuration);
        server.start();
                
        TransportConfiguration clientTransportConfiguration = new TransportConfiguration(InVMConnectorFactory.class.getName());
        ServerLocator serverLocator = HornetQClient.createServerLocatorWithoutHA(clientTransportConfiguration);
        ClientSessionFactory clientSessionFactory = serverLocator.createSessionFactory();
        
        ClientSession session = clientSessionFactory.createSession(false, false, false);
        session.createQueue("HelloQueue", "HelloQueue", true);
        session.close();
        
        session = clientSessionFactory.createSession();
        session.start();
        
        ClientProducer producer = session.createProducer("HelloQueue");
        ClientMessage message = session.createMessage(false);
        message.putStringProperty("text", "hello there");
        producer.send(message);
        
        ClientConsumer consumer = session.createConsumer("HelloQueue");
        ClientMessage m = consumer.receive();
        assertEquals("hello there", m.getStringProperty("text"));
        
        session.stop();
        session.close();
        
        server.stop();
    }
}
