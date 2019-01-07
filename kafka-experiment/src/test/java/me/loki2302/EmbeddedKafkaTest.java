package me.loki2302;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.SystemTime$;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class EmbeddedKafkaTest {
    private TestingServer zooKeeperServer;
    private KafkaServer kafkaServer;

    @Before
    public void startUp() throws Exception {
        zooKeeperServer = new TestingServer(2181);
        zooKeeperServer.start();

        Properties props = new Properties();
        props.setProperty("hostname", "localhost");
        props.setProperty("port", "9092");
        props.setProperty("broker.id", "1");
        props.setProperty("log.dir", "/tmp/embeddedkafka1/");
        props.setProperty("zookeeper.connect", "localhost:2181");
        props.setProperty("advertised.host.name", "localhost");

        KafkaConfig kafkaConfig = new KafkaConfig(props);
        kafkaServer = new KafkaServer(kafkaConfig, SystemTime$.MODULE$);
        kafkaServer.startup();
    }

    @After
    public void shutdown() throws IOException {
        kafkaServer.shutdown();
        kafkaServer.awaitShutdown();
        zooKeeperServer.close();
    }

    @Test
    public void dummy() throws InterruptedException, ExecutionException, BrokenBarrierException {
        int numberOfMessagesConsumed = App.kafkaProducerConsumerHelloWorld();
        assertEquals(1000, numberOfMessagesConsumed);
    }
}
